package com.yvonne.onakawash.service;

import com.yvonne.onakawash.controller.AdaptiveReviewSessionController;
import com.yvonne.onakawash.entity.PracticeSessionEntity;
import com.yvonne.onakawash.entity.PracticeSessionQuestionEntity;
import com.yvonne.onakawash.entity.ReviewAnswerRecordEntity;
import com.yvonne.onakawash.entity.TangoItemEntity;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import com.yvonne.onakawash.repository.ReviewAnswerRecordRepository;
import com.yvonne.onakawash.repository.TangoItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:session-detail-tests;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class AdaptiveReviewSessionDetailServiceTest {

    @Autowired
    private AdaptiveReviewSessionDetailService detailService;

    @Autowired
    private AdaptiveReviewSessionService creationService;

    @Autowired
    private PracticeSessionRepository sessionRepository;

    @Autowired
    private PracticeSessionQuestionRepository questionRepository;

    @Autowired
    private TangoItemRepository tangoRepository;

    @Autowired
    private ReviewAnswerRecordRepository answerRepository;

    private MockMvc mockMvc;
    private String sessionKey;

    private static final String ARI_ID = "detail-test-ari";
    private static final String INU_ID = "detail-test-inu";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new AdaptiveReviewSessionController(
                        creationService,
                        detailService
                )
        ).build();

        sessionKey = UUID.randomUUID().toString();

        tangoRepository.save(new TangoItemEntity(
                ARI_ID,
                "あり",
                "HIRAGANA",
                "蚂蚁",
                "ari",
                "VOCABULARY",
                List.of("ari", "asa", "inu", "ao"),
                List.of("hiragana-a", "hiragana-ri")
        ));

        tangoRepository.save(new TangoItemEntity(
                INU_ID,
                "いぬ",
                "HIRAGANA",
                "狗",
                "inu",
                "VOCABULARY",
                List.of("inu", "ie", "ari", "ao"),
                List.of("hiragana-i", "hiragana-nu")
        ));

        var session = new PracticeSessionEntity();
        session.setSessionKey(sessionKey);
        session.setUserId(1L);
        session.setSessionType("ADAPTIVE_REVIEW");
        session.setPracticeType("ADAPTIVE_REVIEW");
        session.setPracticeMode("ROMAJI_CHOICE");
        session.setTotalQuestions(2);
        sessionRepository.save(session);

        // 故意先保存第二题，验证读取时按题号排序。
        saveQuestion(2, INU_ID);
        saveQuestion(1, ARI_ID);
    }

    @Test
    void returnsQuestionsInSavedOrderWithoutWritingRecords() {
        long sessionsBefore = sessionRepository.count();
        long answersBefore = answerRepository.count();

        var result = detailService.getSessionDetail(sessionKey);

        assertEquals(sessionKey, result.sessionKey());
        assertEquals(2, result.actualQuestionCount());
        assertEquals(
                List.of(1, 2),
                result.questions().stream()
                        .map(question -> question.questionIndex())
                        .toList()
        );

        var first = result.questions().get(0);
        assertEquals(ARI_ID, first.tangoItemId());
        assertEquals("あり", first.displayText());
        assertEquals(
                List.of("ari", "asa", "inu", "ao"),
                first.options()
        );
        assertNull(first.answer());

        assertEquals(INU_ID, result.questions().get(1).tangoItemId());
        assertEquals(sessionsBefore, sessionRepository.count());
        assertEquals(answersBefore, answerRepository.count());
    }

    @Test
    void returnsSavedAnswerAndLeavesUnansweredQuestionWithoutAnswer()
            throws Exception {
        answerRepository.saveAndFlush(new ReviewAnswerRecordEntity(
                1L,
                sessionKey,
                1,
                ARI_ID,
                "asa",
                "ari",
                false,
                LocalDateTime.of(2026, 9, 11, 10, 0),
                1200
        ));

        mockMvc.perform(get(
                        "/adaptive-review/sessions/{sessionKey}",
                        sessionKey
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actualQuestionCount").value(2))
                .andExpect(jsonPath(
                        "$.questions[0].answer.selectedRomaji"
                ).value("asa"))
                .andExpect(jsonPath(
                        "$.questions[0].answer.correctRomaji"
                ).value("ari"))
                .andExpect(jsonPath(
                        "$.questions[0].answer.isCorrect"
                ).value(false))
                .andExpect(jsonPath(
                        "$.questions[1].answer"
                ).doesNotExist())
                .andExpect(jsonPath(
                        "$.questions[1].correctRomaji"
                ).doesNotExist());
    }

    @Test
    void unansweredQuestionsDoNotExposeCorrectAnswerField() throws Exception {
        mockMvc.perform(get(
                        "/adaptive-review/sessions/{sessionKey}",
                        sessionKey
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions[0].displayText").value("あり"))
                .andExpect(jsonPath("$.questions[0].options.length()").value(4))
                .andExpect(jsonPath("$.questions[0].answer").doesNotExist())
                .andExpect(jsonPath(
                        "$.questions[0].correctRomaji"
                ).doesNotExist())
                .andExpect(jsonPath("$.questions[1].answer").doesNotExist());
    }

    @Test
    void missingSessionReturns404() throws Exception {
        mockMvc.perform(get(
                        "/adaptive-review/sessions/{sessionKey}",
                        "missing-session"
                ))
                .andExpect(status().isNotFound());
    }

    @Test
    void ordinaryKanaSessionReturns400() throws Exception {
        var session = sessionRepository.findBySessionKey(sessionKey)
                .orElseThrow();

        session.setSessionType("KANA_PRACTICE");
        sessionRepository.saveAndFlush(session);

        mockMvc.perform(get(
                        "/adaptive-review/sessions/{sessionKey}",
                        sessionKey
                ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingAssignedVocabularyIsReportedAsAnError() {
        tangoRepository.deleteById(ARI_ID);
        tangoRepository.flush();

        var error = assertThrows(
                ResponseStatusException.class,
                () -> detailService.getSessionDetail(sessionKey)
        );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                error.getStatusCode()
        );
    }

    private void saveQuestion(int index, String tangoItemId) {
        var question = new PracticeSessionQuestionEntity();
        question.setSessionKey(sessionKey);
        question.setQuestionIndex(index);
        question.setTangoItemId(tangoItemId);
        questionRepository.save(question);
    }
}