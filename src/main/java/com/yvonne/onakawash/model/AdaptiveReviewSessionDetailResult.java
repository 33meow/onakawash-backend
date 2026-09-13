package com.yvonne.onakawash.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;



public record AdaptiveReviewSessionDetailResult(
        String sessionKey,
        int actualQuestionCount,
        List<Question> questions,
        boolean coverageAvailable,
        List<CoverageKana> coverage
) {
    public record Question(
            int questionIndex,
            String tangoItemId,
            String displayText,
            List<String> options,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            SavedAnswer answer
    ) {
    }

    public record SavedAnswer(
            String selectedRomaji,
            String correctRomaji,
            boolean isCorrect
    ) {
    }

    public record CoverageKana(
            String kanaItemId,
            String kana,
            String status
    ) {
    }
}