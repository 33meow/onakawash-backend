//PracticeSessionQuestionEntity 是 数据库存储版。
//AdaptiveReviewSessionQuestionResult 是 API 返回版。
package com.yvonne.onakawash.model;

public class AdaptiveReviewSessionQuestionResult {

    // 这道题在本轮 Adaptive Review session 里的顺序。
    // 1 = 第一题，2 = 第二题。
    private final int questionIndex;

    // 这道题使用哪一个 TangoItem。
    // 前端以后可以根据这个 id 知道本题对应哪张词卡。
    private final String tangoItemId;

    public AdaptiveReviewSessionQuestionResult(
            int questionIndex,
            String tangoItemId
    ) {
        this.questionIndex = questionIndex;
        this.tangoItemId = tangoItemId;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public String getTangoItemId() {
        return tangoItemId;
    }
}