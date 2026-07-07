package com.yvonne.onakawash.model;

import java.util.List;

public class AdaptiveReviewPreviewResult {
    private String previewStatus;
    private int weakKanaCount;
    private long distinctAvailableTangoItemCount;
    private int theoreticalQuestionCount;
    private List<AdaptiveReviewPreviewKanaResult> weakKanaWithAvailableContent;
    private List<AdaptiveReviewPreviewKanaResult> weakKanaWithoutAvailableContent;

    public AdaptiveReviewPreviewResult(
            //以后可能返回：
            //ready
            //no_weak_kana
            //no_available_tango_content
            String previewStatus,
            //比如当前有 3 个 weak kana，这里就是 3。
            int weakKanaCount,
            //这里强调 distinct，因为一个 TangoItem 可能覆盖多个 weak kana，但总内容数只能算一次。
            long distinctAvailableTangoItemCount,
            //后面 service 会算
            int theoreticalQuestionCount,
            //里面放 availableTangoItemCount > 0 的 weak kana。
            List<AdaptiveReviewPreviewKanaResult> weakKanaWithAvailableContent,
            //里面放 availableTangoItemCount == 0 的 weak kana。
            List<AdaptiveReviewPreviewKanaResult> weakKanaWithoutAvailableContent
    ) {
        this.previewStatus = previewStatus;
        this.weakKanaCount = weakKanaCount;
        this.distinctAvailableTangoItemCount = distinctAvailableTangoItemCount;
        this.theoreticalQuestionCount = theoreticalQuestionCount;
        this.weakKanaWithAvailableContent = weakKanaWithAvailableContent;
        this.weakKanaWithoutAvailableContent = weakKanaWithoutAvailableContent;
    }

    public String getPreviewStatus() {
        return previewStatus;
    }

    public int getWeakKanaCount() {
        return weakKanaCount;
    }

    public long getDistinctAvailableTangoItemCount() {
        return distinctAvailableTangoItemCount;
    }

    public int getTheoreticalQuestionCount() {
        return theoreticalQuestionCount;
    }

    public List<AdaptiveReviewPreviewKanaResult> getWeakKanaWithAvailableContent() {
        return weakKanaWithAvailableContent;
    }

    public List<AdaptiveReviewPreviewKanaResult> getWeakKanaWithoutAvailableContent() {
        return weakKanaWithoutAvailableContent;
    }
}
