package com.yvonne.onakawash.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

// 返回给前端的本轮练习详情。
// record 会自动生成构造方法和字段读取方法。
public record AdaptiveReviewSessionDetailResult(
        String sessionKey,
        int actualQuestionCount,
        List<Question> questions
) {
    public record Question(
            int questionIndex,
            String tangoItemId,
            String displayText,
            List<String> options,

            // 未作答时，不向前端返回 answer 字段。
            @JsonInclude(JsonInclude.Include.NON_NULL)
            SavedAnswer answer
    ) {
    }

    // 只有已经保存过的答案，才包含正确读音和判题结果。
    public record SavedAnswer(
            String selectedRomaji,
            String correctRomaji,
            boolean isCorrect
    ) {
    }
}