package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.PracticeSessionEntity;
import com.yvonne.onakawash.entity.PracticeSessionQuestionEntity;
import com.yvonne.onakawash.model.ReviewAnswerRequest;
import com.yvonne.onakawash.repository.AnswerRecordRepository;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import com.yvonne.onakawash.repository.ReviewAnswerRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.yvonne.onakawash.controller.ReviewAnswerRecordController;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import com.yvonne.onakawash.entity.ReviewAnswerRecordEntity;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.model.KanaMasteryResult;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:be5-review-answer-tests",
        "spring.jpa.show-sql=false"
})
class ReviewAnswerRecordServiceTest {

    private static final String SESSION_KEY = "be5-test-session";
    private static final String TANGO_ITEM_ID = "tango-ari";
    private static final LocalDateTime ANSWERED_AT =
            LocalDateTime.of(2026, 9, 9, 10, 0);

    @Autowired
    private ReviewAnswerRecordService service;

    @MockitoSpyBean
    private ReviewAnswerRecordRepository reviewAnswerRepository;

    @Autowired
    private PracticeSessionRepository sessionRepository;

    @Autowired
    private PracticeSessionQuestionRepository questionRepository;

    @Autowired
    private AnswerRecordRepository kanaAnswerRepository;

    @Autowired
    private KanaMasteryService kanaMasteryService;

    @Autowired
    private AnswerRecordService ordinaryAnswerService;

    @BeforeEach
    void setUp() {
        // 只清理当前测试进程的专用内存数据库。
        reviewAnswerRepository.deleteAll();
        questionRepository.deleteAll();
        sessionRepository.deleteAll();
        kanaAnswerRepository.deleteAll();

        // 准备一轮复习，模拟 BE-4 已保存的会话。
        var session = new PracticeSessionEntity();
        session.setUserId(1L);
        session.setSessionKey(SESSION_KEY);
        session.setSessionType("ADAPTIVE_REVIEW");
        session.setPracticeType("ADAPTIVE_REVIEW");
        session.setPracticeMode("ROMAJI_CHOICE");
        session.setTotalQuestions(1);
        sessionRepository.saveAndFlush(session);

        // 这轮第一题是「あり」，题库由 data.sql 初始化。
        var question = new PracticeSessionQuestionEntity();
        question.setSessionKey(SESSION_KEY);
        question.setQuestionIndex(1);
        question.setTangoItemId(TANGO_ITEM_ID);
        questionRepository.saveAndFlush(question);
    }

    @Test
    void savesCorrectAnswerWithAllRequiredFields() {
        // 执行：用户选择正确答案 ari。
        var result = service.saveAnswer(request("ari"));

        // 从数据库重新读取，确认确实保存成功。
        var saved = reviewAnswerRepository
                .findBySessionKeyAndQuestionIndex(SESSION_KEY, 1)
                .orElseThrow();

        assertNotNull(result.getId());
        assertEquals(result.getId(), saved.getId());
        assertEquals(Long.valueOf(1L), saved.getUserId());
        assertEquals(SESSION_KEY, saved.getSessionKey());
        assertEquals(Integer.valueOf(1), saved.getQuestionIndex());
        assertEquals(TANGO_ITEM_ID, saved.getTangoItemId());
        assertEquals("ari", saved.getSelectedRomaji());
        assertEquals("ari", saved.getCorrectRomaji());
        assertEquals(Boolean.TRUE, saved.getIsCorrect());
        assertEquals(ANSWERED_AT, saved.getAnsweredAt());
        assertEquals(Integer.valueOf(1200), saved.getResponseTimeMs());
        assertNotNull(saved.getCreatedAt());

        // 一个词只产生一条词汇答案，不产生假名答案。
        assertEquals(1L, reviewAnswerRepository.count());
        assertEquals(0L, kanaAnswerRepository.count());
    }

    @Test
    void savesWrongAnswerAndCalculatesCorrectnessOnBackend() {
        // asa 是题目提供的合法选项，但不是正确答案。
        service.saveAnswer(request("asa"));

        var saved = reviewAnswerRepository
                .findBySessionKeyAndQuestionIndex(SESSION_KEY, 1)
                .orElseThrow();

        assertEquals("asa", saved.getSelectedRomaji());
        assertEquals("ari", saved.getCorrectRomaji());
        assertEquals(Boolean.FALSE, saved.getIsCorrect());
        assertEquals(1L, reviewAnswerRepository.count());
        assertEquals(0L, kanaAnswerRepository.count());
    }

    @Test
    void rejectsRepeatedSubmissionWithoutOverwritingFirstAnswer() {
        // 第一次成功保存。
        service.saveAnswer(request("ari"));

        // 第二次试图改答案，应被拒绝。
        var exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveAnswer(request("asa"))
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals(1L, reviewAnswerRepository.count());

        // 原来的答案必须保留。
        var saved = reviewAnswerRepository
                .findBySessionKeyAndQuestionIndex(SESSION_KEY, 1)
                .orElseThrow();

        assertEquals("ari", saved.getSelectedRomaji());
        assertEquals(Boolean.TRUE, saved.getIsCorrect());
    }

    @Test
    void rejectsNonExistentSession() {
        var invalidRequest = new ReviewAnswerRequest(
                "missing-session",
                1,
                TANGO_ITEM_ID,
                "ari",
                ANSWERED_AT,
                1200
        );

        assertRejectedWithoutSaving(
                invalidRequest,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void rejectsOrdinaryKanaPracticeSession() {
        // 会话确实存在，但它是普通假名练习。
        var session = sessionRepository
                .findBySessionKey(SESSION_KEY)
                .orElseThrow();

        session.setSessionType("KANA_PRACTICE");
        sessionRepository.saveAndFlush(session);

        assertRejectedWithoutSaving(
                request("ari"),
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void rejectsQuestionIndexNotAssignedToSession() {
        // 测试会话只有第 1 题，不应该接受第 2 题。
        var invalidRequest = new ReviewAnswerRequest(
                SESSION_KEY,
                2,
                TANGO_ITEM_ID,
                "ari",
                ANSWERED_AT,
                1200
        );

        assertRejectedWithoutSaving(
                invalidRequest,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void rejectsTangoItemThatDoesNotMatchAssignedQuestion() {
        // 第 1 题是 tango-ari，不能偷偷提交 tango-inu。
        var invalidRequest = new ReviewAnswerRequest(
                SESSION_KEY,
                1,
                "tango-inu",
                "inu",
                ANSWERED_AT,
                1200
        );

        assertRejectedWithoutSaving(
                invalidRequest,
                HttpStatus.BAD_REQUEST
        );
    }

    private void assertRejectedWithoutSaving(
            ReviewAnswerRequest invalidRequest,
            HttpStatus expectedStatus
    ) {
        var exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveAnswer(invalidRequest)
        );

        assertEquals(expectedStatus, exception.getStatusCode());

        // 不仅要返回错误，还必须确认没有偷偷保存记录。
        assertEquals(0L, reviewAnswerRepository.count());
        assertEquals(0L, kanaAnswerRepository.count());
    }


    @Test
    void postEndpointAcceptsJsonAndReturnsSavedAnswer() throws Exception {
        // 使用真实 Service，测试 Controller 的请求映射和 JSON 转换。
        var mockMvc = MockMvcBuilders.standaloneSetup(
                new ReviewAnswerRecordController(service)
        ).build();

        String body = """
                {
                    "sessionKey": "be5-test-session",
                    "questionIndex": 1,
                    "tangoItemId": "tango-ari",
                    "selectedRomaji": "ari",
                    "answeredAt": "2026-09-09T10:00:00",
                    "responseTimeMs": 1200
                }
                """;

        mockMvc.perform(post("/adaptive-review/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.sessionKey").value(SESSION_KEY))
                .andExpect(jsonPath("$.questionIndex").value(1))
                .andExpect(jsonPath("$.tangoItemId").value(TANGO_ITEM_ID))
                .andExpect(jsonPath("$.selectedRomaji").value("ari"))
                .andExpect(jsonPath("$.correctRomaji").value("ari"))
                .andExpect(jsonPath("$.isCorrect").value(true));

        assertEquals(1L, reviewAnswerRepository.count());
    }

    @Test
    void postEndpointReturnsNotFoundForMissingSession() throws Exception {
        var mockMvc = MockMvcBuilders.standaloneSetup(
                new ReviewAnswerRecordController(service)
        ).build();

        String body = """
                {
                    "sessionKey": "missing-session",
                    "questionIndex": 1,
                    "tangoItemId": "tango-ari",
                    "selectedRomaji": "ari",
                    "answeredAt": "2026-09-09T10:00:00",
                    "responseTimeMs": 1200
                }
                """;

        mockMvc.perform(post("/adaptive-review/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

        assertEquals(0L, reviewAnswerRepository.count());
    }

    @Test
    void concurrentSubmissionsSaveOnlyOneAnswer() throws Exception {
        // 两个请求都查完之后，才一起继续。
        var barrier = new CyclicBarrier(2);

        doAnswer(invocation -> {
            // 模拟两个请求都刚刚查到“没有答案”。
            // 等两个请求都到达这里，再一起继续保存。
            barrier.await(10, TimeUnit.SECONDS);

            return Optional.<ReviewAnswerRecordEntity>empty();
        }).when(reviewAnswerRepository)
                .findBySessionKeyAndQuestionIndex(SESSION_KEY, 1);
        var executor = Executors.newFixedThreadPool(2);

        try {
            var first = executor.submit(
                    () -> submitAnswerAndGetStatus("ari")
            );
            var second = executor.submit(
                    () -> submitAnswerAndGetStatus("asa")
            );

            int firstStatus = first.get(20, TimeUnit.SECONDS);
            int secondStatus = second.get(20, TimeUnit.SECONDS);

            // 谁先成功不确定，但必须恰好一个成功、一个冲突。
            var statuses = List.of(firstStatus, secondStatus)
                    .stream()
                    .sorted()
                    .toList();

            assertEquals(List.of(201, 409), statuses);

            var savedAnswers = reviewAnswerRepository.findAll();
            assertEquals(1, savedAnswers.size());

            var saved = savedAnswers.get(0);
            String winningAnswer = firstStatus == 201 ? "ari" : "asa";

            assertEquals(winningAnswer, saved.getSelectedRomaji());
            assertEquals(
                    Boolean.valueOf("ari".equals(winningAnswer)),
                    saved.getIsCorrect()
            );
            assertEquals(0L, kanaAnswerRepository.count());
        } finally {
            // 结束测试线程，并移除本次测试设置的暂停行为。
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            reset(reviewAnswerRepository);
        }
    }

    private int submitAnswerAndGetStatus(String selectedRomaji) {
        try {
            service.saveAnswer(request(selectedRomaji));
            // 这里用 201 表示 Service 成功返回，并非实际 HTTP 请求。
            return HttpStatus.CREATED.value();
        } catch (ResponseStatusException exception) {
            return exception.getStatusCode().value();
        }
    }

    @Test
    void vocabularyAnswerDoesNotChangeKanaMastery() {
        // 先给「あ」准备 3 条错误记录，确保已有真实掌握度数据。
        for (int i = 0; i < 3; i++) {
            ordinaryAnswerService.saveAnswerRecord(
                    ordinaryKanaAnswer()
            );
        }

        var before = masteryOf("hiragana-a");

        assertEquals(3, before.getEvidenceCount());
        assertEquals("weak", before.getStatus());

        // 词汇「あり」答对，也不能直接推断「あ」已经掌握。
        service.saveAnswer(request("ari"));

        var after = masteryOf("hiragana-a");

        assertEquals(before.getEvidenceCount(), after.getEvidenceCount());
        assertEquals(before.getWeakScore(), after.getWeakScore(), 0.000001);
        assertEquals(before.getStatus(), after.getStatus());

        assertEquals(3L, kanaAnswerRepository.count());
        assertEquals(1L, reviewAnswerRepository.count());
    }

    @Test
    void ordinaryKanaAnswersStillSaveSeparately() {
        // 先保存词汇答案，再使用原来的普通假名保存流程。
        service.saveAnswer(request("ari"));

        var result = ordinaryAnswerService.saveAnswerRecord(
                ordinaryKanaAnswer()
        );

        var saved = kanaAnswerRepository.findById(result.getId())
                .orElseThrow();

        assertNotNull(saved.getId());
        assertEquals("ordinary-test-session", saved.getSessionKey());
        assertEquals("HIRAGANA", saved.getPracticeType());
        assertEquals("ROMAJI_CHOICE", saved.getPracticeMode());
        assertEquals("hiragana-a", saved.getKanaItemId());
        assertEquals("あ", saved.getKana());
        assertEquals("a", saved.getCorrectRomaji());
        assertEquals("i", saved.getSelectedRomaji());
        assertEquals(Boolean.FALSE, saved.getIsCorrect());
        assertEquals(ANSWERED_AT, saved.getAnsweredAt());
        assertEquals(Integer.valueOf(1200), saved.getResponseTimeMs());
        assertNotNull(saved.getCreatedAt());

        // 原来的按会话读取功能仍可使用。
        var history = ordinaryAnswerService
                .getAnswerRecordsBySessionKey("ordinary-test-session");

        assertEquals(1, history.size());
        assertEquals(saved.getId(), history.get(0).getId());

        // 两种答案各存一条，没有混在一起。
        assertEquals(1L, kanaAnswerRepository.count());
        assertEquals(1L, reviewAnswerRepository.count());
    }

    private AnswerRecordEntity ordinaryKanaAnswer() {
        var answer = new AnswerRecordEntity();
        answer.setUserId(1L);
        answer.setSessionKey("ordinary-test-session");
        answer.setPracticeType("HIRAGANA");
        answer.setPracticeMode("ROMAJI_CHOICE");
        answer.setKanaItemId("hiragana-a");
        answer.setKana("あ");
        answer.setCorrectRomaji("a");
        answer.setSelectedRomaji("i");
        answer.setIsCorrect(false);
        answer.setAnsweredAt(ANSWERED_AT);
        answer.setResponseTimeMs(1200);
        return answer;
    }

    private KanaMasteryResult masteryOf(String kanaItemId) {
        return kanaMasteryService.getKanaMasteryResults()
                .stream()
                .filter(result -> kanaItemId.equals(result.getKanaItemId()))
                .findFirst()
                .orElseThrow();
    }

    private ReviewAnswerRequest request(String selectedRomaji) {
        return new ReviewAnswerRequest(
                SESSION_KEY,
                1,
                TANGO_ITEM_ID,
                selectedRomaji,
                ANSWERED_AT,
                1200
        );
    }
}