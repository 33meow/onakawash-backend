package com.yvonne.onakawash.model;

import java.util.List;

public class AdaptiveReviewSessionResult {

    // 创建 session 的结果状态。
    // created = 成功创建了 PracticeSession。
    // no_available_tango_content = 没有可用 TangoItem，所以没有创建 PracticeSession。
    private final String sessionStatus;

    // PracticeSession 保存到数据库后生成的 id。
    // 如果没有创建 session，这里可以是 null。
    private final Long practiceSessionId;

    // 这一轮 session 的唯一标识。
    // 后面前端答题、查题目顺序，都可以靠 sessionKey 找到这一轮。
    private final String sessionKey;

    // 实际创建了几道题。
    // 最多是 10，但如果可用 TangoItem 只有 3 个，那这里就是 3。
    private final int actualQuestionCount;

    // 本轮实际选中的题目顺序。
    // 每一项里面有 questionIndex 和 tangoItemId。
    private final List<AdaptiveReviewSessionQuestionResult> selectedQuestions;

    // 本轮实际覆盖到的 weak kana。
    // 比如 tango-ari 覆盖 hiragana-a 和 hiragana-ri。
    private final List<AdaptiveReviewPreviewKanaResult> actualWeakKanaCoverage;

    // 因为最多 10 题限制，这轮没排进去、留到以后复习的 weak kana。
    private final List<AdaptiveReviewPreviewKanaResult> deferredWeakKana;

    // 虽然是 weak，但目前没有任何 TangoItem 可以练的 kana。
    private final List<AdaptiveReviewPreviewKanaResult> weakKanaWithoutAvailableContent;

    public AdaptiveReviewSessionResult(
            String sessionStatus,
            Long practiceSessionId,
            String sessionKey,
            int actualQuestionCount,
            List<AdaptiveReviewSessionQuestionResult> selectedQuestions,
            List<AdaptiveReviewPreviewKanaResult> actualWeakKanaCoverage,
            List<AdaptiveReviewPreviewKanaResult> deferredWeakKana,
            List<AdaptiveReviewPreviewKanaResult> weakKanaWithoutAvailableContent
    ) {
        this.sessionStatus = sessionStatus;
        this.practiceSessionId = practiceSessionId;
        this.sessionKey = sessionKey;
        this.actualQuestionCount = actualQuestionCount;
        this.selectedQuestions = selectedQuestions;
        this.actualWeakKanaCoverage = actualWeakKanaCoverage;
        this.deferredWeakKana = deferredWeakKana;
        this.weakKanaWithoutAvailableContent = weakKanaWithoutAvailableContent;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public Long getPracticeSessionId() {
        return practiceSessionId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public int getActualQuestionCount() {
        return actualQuestionCount;
    }

    public List<AdaptiveReviewSessionQuestionResult> getSelectedQuestions() {
        return selectedQuestions;
    }

    public List<AdaptiveReviewPreviewKanaResult> getActualWeakKanaCoverage() {
        return actualWeakKanaCoverage;
    }

    public List<AdaptiveReviewPreviewKanaResult> getDeferredWeakKana() {
        return deferredWeakKana;
    }

    public List<AdaptiveReviewPreviewKanaResult> getWeakKanaWithoutAvailableContent() {
        return weakKanaWithoutAvailableContent;
    }
}