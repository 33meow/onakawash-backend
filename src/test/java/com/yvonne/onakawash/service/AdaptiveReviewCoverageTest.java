package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.PracticeSessionEntity;
import com.yvonne.onakawash.entity.PracticeSessionQuestionEntity;
import com.yvonne.onakawash.entity.TangoItemEntity;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewKanaResult;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewResult;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import com.yvonne.onakawash.repository.TangoItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:coverage-tests;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class AdaptiveReviewCoverageTest {

    // 只模拟练习前的预览数据，保存和读取仍使用真实数据库。
    @MockitoBean
    private AdaptiveReviewPreviewService previewService;

    @Autowired
    private AdaptiveReviewSessionService creationService;

    @Autowired
    private AdaptiveReviewSessionDetailService detailService;

    @Autowired
    private TangoItemRepository tangoRepository;

    @Autowired
    private PracticeSessionRepository sessionRepository;

    @Autowired
    private PracticeSessionQuestionRepository questionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void separatesCoveredDeferredAndMissingContentAndPreservesSnapshot() {
        List<AdaptiveReviewPreviewKanaResult> available = new ArrayList<>();

        // 11 个词，每个对应一个不同的薄弱假名。
        for (int index = 1; index <= 11; index++) {
            String suffix = String.format("%02d", index);
            String kanaId = "coverage-kana-" + suffix;

            available.add(kana(kanaId, "假名" + suffix, 1));
            saveWord("coverage-word-" + suffix, List.of(kanaId));
        }

        var missing = kana("coverage-missing", "未收录", 0);

        when(previewService.getPreview()).thenReturn(
                new AdaptiveReviewPreviewResult(
                        "ready",
                        12,
                        11,
                        10,
                        available,
                        List.of(missing)
                )
        );

        var created = creationService.createAdaptiveReviewSession();

        assertEquals(10, created.getActualQuestionCount());
        assertEquals(10, created.getActualWeakKanaCoverage().size());
        assertEquals(1, created.getDeferredWeakKana().size());
        assertEquals(
                "coverage-kana-11",
                created.getDeferredWeakKana().get(0).getKanaItemId()
        );
        assertEquals(1, created.getWeakKanaWithoutAvailableContent().size());

        // 强制写入并清除内存中的实体，后续从数据库重新读取。
        entityManager.flush();
        entityManager.clear();

        var before = detailService.getSessionDetail(created.getSessionKey());

        assertTrue(before.coverageAvailable());
        assertEquals(12, before.coverage().size());

        Map<String, String> statuses = before.coverage().stream()
                .collect(Collectors.toMap(
                        item -> item.kanaItemId(),
                        item -> item.status()
                ));

        for (int index = 1; index <= 10; index++) {
            assertEquals(
                    "covered",
                    statuses.get("coverage-kana-" + String.format("%02d", index))
            );
        }

        assertEquals("deferred", statuses.get("coverage-kana-11"));
        assertEquals("no_content", statuses.get("coverage-missing"));

        // 后来词库补齐了缺失内容、预览也变了。
        saveWord("coverage-new-word", List.of("coverage-missing"));
        when(previewService.getPreview()).thenReturn(
                new AdaptiveReviewPreviewResult(
                        "no_weak_kana",
                        0,
                        0,
                        0,
                        List.of(),
                        List.of()
                )
        );

        entityManager.flush();
        entityManager.clear();
        clearInvocations(previewService);

        var after = detailService.getSessionDetail(created.getSessionKey());

        // 原来这一轮的覆盖结果必须保持不变。
        assertEquals(before.coverage(), after.coverage());
        verifyNoInteractions(previewService);
    }

    @Test
    void oneQuestionCanCoverMultipleWeakKana() {
        saveWord(
                "coverage-shared-word",
                List.of("coverage-a", "coverage-b", "coverage-normal")
        );

        when(previewService.getPreview()).thenReturn(
                new AdaptiveReviewPreviewResult(
                        "ready",
                        2,
                        1,
                        1,
                        List.of(
                                kana("coverage-a", "あ", 1),
                                kana("coverage-b", "り", 1)
                        ),
                        List.of()
                )
        );

        var created = creationService.createAdaptiveReviewSession();

        assertEquals(1, created.getActualQuestionCount());
        assertEquals(2, created.getActualWeakKanaCoverage().size());
        assertTrue(created.getDeferredWeakKana().isEmpty());

        entityManager.flush();
        entityManager.clear();

        var detail = detailService.getSessionDetail(created.getSessionKey());

        assertEquals(2, detail.coverage().size());
        assertTrue(detail.coverage().stream().allMatch(
                item -> item.status().equals("covered")
        ));
        assertFalse(detail.coverage().stream().anyMatch(
                item -> item.kanaItemId().equals("coverage-normal")
        ));
    }

    @Test
    void noWeakKanaDoesNotCreateEmptySession() {
        when(previewService.getPreview()).thenReturn(
                new AdaptiveReviewPreviewResult(
                        "no_weak_kana",
                        0,
                        0,
                        0,
                        List.of(),
                        List.of()
                )
        );

        assertNoSessionCreated();
    }

    @Test
    void noAvailableContentDoesNotCreateEmptySession() {
        when(previewService.getPreview()).thenReturn(
                new AdaptiveReviewPreviewResult(
                        "no_available_tango_content",
                        1,
                        0,
                        0,
                        List.of(),
                        List.of(kana("coverage-missing", "未收录", 0))
                )
        );

        assertNoSessionCreated();
    }

    @Test
    void oldSessionReportsCoverageUnavailable() {
        saveWord("coverage-old-word", List.of("coverage-old-kana"));

        String sessionKey = UUID.randomUUID().toString();
        var session = new PracticeSessionEntity();
        session.setSessionKey(sessionKey);
        session.setUserId(1L);
        session.setSessionType("ADAPTIVE_REVIEW");
        session.setTotalQuestions(1);

        // 模拟旧记录：没有调用 captureKanaCoverage。
        sessionRepository.save(session);

        var question = new PracticeSessionQuestionEntity();
        question.setSessionKey(sessionKey);
        question.setQuestionIndex(1);
        question.setTangoItemId("coverage-old-word");
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        var detail = detailService.getSessionDetail(sessionKey);

        assertFalse(detail.coverageAvailable());
        assertTrue(detail.coverage().isEmpty());
        assertEquals(1, detail.questions().size());
        verifyNoInteractions(previewService);
    }

    private void assertNoSessionCreated() {
        long sessionsBefore = sessionRepository.count();
        long questionsBefore = questionRepository.count();

        var result = creationService.createAdaptiveReviewSession();

        assertEquals("no_available_tango_content", result.getSessionStatus());
        assertNull(result.getSessionKey());
        assertEquals(0, result.getActualQuestionCount());
        assertEquals(sessionsBefore, sessionRepository.count());
        assertEquals(questionsBefore, questionRepository.count());
    }

    private AdaptiveReviewPreviewKanaResult kana(
            String id,
            String text,
            long availableCount
    ) {
        return new AdaptiveReviewPreviewKanaResult(
                id,
                text,
                3,
                0.8,
                availableCount
        );
    }

    private void saveWord(String id, List<String> coveredKanaIds) {
        tangoRepository.save(new TangoItemEntity(
                id,
                "あり",
                "HIRAGANA",
                "蚂蚁",
                "ari",
                "VOCABULARY",
                List.of("ari", "asa", "inu", "ao"),
                coveredKanaIds
        ));
    }
}