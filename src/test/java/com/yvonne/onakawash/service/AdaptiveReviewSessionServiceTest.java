package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.entity.PracticeSessionQuestionEntity;
import com.yvonne.onakawash.model.AdaptiveReviewSessionResult;
import com.yvonne.onakawash.repository.AnswerRecordRepository;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AdaptiveReviewSessionServiceTest {

    @Autowired
    private AdaptiveReviewSessionService adaptiveReviewSessionService;

    @Autowired
    private AnswerRecordRepository answerRecordRepository;

    @Autowired
    private PracticeSessionRepository practiceSessionRepository;

    @Autowired
    private PracticeSessionQuestionRepository practiceSessionQuestionRepository;

    @BeforeEach
    void setUp() {
        practiceSessionQuestionRepository.deleteAll();
        practiceSessionRepository.deleteAll();
        answerRecordRepository.deleteAll();
    }

    @Test
    void createsAdaptiveReviewSessionWithFixedQuestionSequence() {
        saveAnswerRecord("hiragana-a", "あ", false, 1800);
        saveAnswerRecord("hiragana-a", "あ", false, 1900);
        saveAnswerRecord("hiragana-a", "あ", true, 4500);

        AdaptiveReviewSessionResult result =
                adaptiveReviewSessionService.createAdaptiveReviewSession();

        assertEquals("created", result.getSessionStatus());
        assertNotNull(result.getPracticeSessionId());
        assertNotNull(result.getSessionKey());
        assertEquals(1, result.getActualQuestionCount());
        assertEquals(1, result.getSelectedQuestions().size());
        assertEquals(1, result.getActualWeakKanaCoverage().size());

        List<PracticeSessionQuestionEntity> savedQuestions =
                practiceSessionQuestionRepository.findBySessionKeyOrderByQuestionIndexAsc(
                        result.getSessionKey()
                );

        assertEquals(1, savedQuestions.size());
        assertEquals(1, savedQuestions.get(0).getQuestionIndex());
        assertEquals(
                result.getSelectedQuestions().get(0).getTangoItemId(),
                savedQuestions.get(0).getTangoItemId()
        );
    }

    private void saveAnswerRecord(
            String kanaItemId,
            String kana,
            boolean isCorrect,
            int responseTimeMs
    ) {
        AnswerRecordEntity answerRecord = new AnswerRecordEntity();

        answerRecord.setUserId(1L);
        answerRecord.setSessionKey("test-session");
        answerRecord.setPracticeType("HIRAGANA");
        answerRecord.setPracticeMode("ROMAJI_CHOICE");
        answerRecord.setKanaItemId(kanaItemId);
        answerRecord.setKana(kana);
        answerRecord.setCorrectRomaji("a");
        answerRecord.setSelectedRomaji(isCorrect ? "a" : "i");
        answerRecord.setIsCorrect(isCorrect);
        answerRecord.setResponseTimeMs(responseTimeMs);
        answerRecord.setAnsweredAt(LocalDateTime.now());

        answerRecordRepository.save(answerRecord);
    }
}