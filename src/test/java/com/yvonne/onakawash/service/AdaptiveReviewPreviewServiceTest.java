package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewResult;
import com.yvonne.onakawash.repository.AnswerRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdaptiveReviewPreviewServiceTest {

    @Autowired
    private AnswerRecordRepository answerRecordRepository;

    @Autowired
    private AdaptiveReviewPreviewService adaptiveReviewPreviewService;

    @BeforeEach
    void cleanAnswerRecords() {
        answerRecordRepository.deleteAll();
    }
    @Test
    void returnsReadyPreviewWhenWeakKanaHasAvailableTangoItem() {
        saveAnswerRecord("hiragana-a", false, 2500, LocalDateTime.of(2026, 7, 7, 18, 0));
        saveAnswerRecord("hiragana-a", false, 3100, LocalDateTime.of(2026, 7, 7, 18, 1));
        saveAnswerRecord("hiragana-a", false, 1800, LocalDateTime.of(2026, 7, 7, 18, 2));

        AdaptiveReviewPreviewResult result = adaptiveReviewPreviewService.getPreview();

        assertEquals("ready", result.getPreviewStatus());
        assertEquals(1, result.getWeakKanaCount());
        assertEquals(1, result.getDistinctAvailableTangoItemCount());
        assertEquals(1, result.getTheoreticalQuestionCount());
        assertEquals(1, result.getWeakKanaWithAvailableContent().size());
        assertEquals(0, result.getWeakKanaWithoutAvailableContent().size());
    }

    @Test
    void returnsNoWeakKanaWhenThereAreNoWeakKana() {
        AdaptiveReviewPreviewResult result = adaptiveReviewPreviewService.getPreview();

        assertEquals("no_weak_kana", result.getPreviewStatus());
        assertEquals(0, result.getWeakKanaCount());
        assertEquals(0, result.getDistinctAvailableTangoItemCount());
        assertEquals(0, result.getTheoreticalQuestionCount());
        assertEquals(0, result.getWeakKanaWithAvailableContent().size());
        assertEquals(0, result.getWeakKanaWithoutAvailableContent().size());
    }

    @Test
    void returnsNoAvailableTangoContentWhenWeakKanaHasNoTangoItem() {
        saveAnswerRecord("hiragana-o", false, 2500, LocalDateTime.of(2026, 7, 7, 18, 10));
        saveAnswerRecord("hiragana-o", false, 3100, LocalDateTime.of(2026, 7, 7, 18, 11));
        saveAnswerRecord("hiragana-o", false, 1800, LocalDateTime.of(2026, 7, 7, 18, 12));

        AdaptiveReviewPreviewResult result = adaptiveReviewPreviewService.getPreview();

        assertEquals("no_available_tango_content", result.getPreviewStatus());
        assertEquals(1, result.getWeakKanaCount());
        assertEquals(0, result.getDistinctAvailableTangoItemCount());
        assertEquals(0, result.getTheoreticalQuestionCount());
        assertEquals(0, result.getWeakKanaWithAvailableContent().size());
        assertEquals(1, result.getWeakKanaWithoutAvailableContent().size());
    }

    private void saveAnswerRecord(
            String kanaItemId,
            boolean isCorrect,
            int responseTimeMs,
            LocalDateTime answeredAt
    ) {
        AnswerRecordEntity record = new AnswerRecordEntity();
        record.setPracticeType("HIRAGANA");
        record.setPracticeMode("ROMAJI_CHOICE");
        record.setKanaItemId(kanaItemId);
        record.setIsCorrect(isCorrect);
        record.setResponseTimeMs(responseTimeMs);
        record.setAnsweredAt(answeredAt);

        answerRecordRepository.save(record);
    }
}