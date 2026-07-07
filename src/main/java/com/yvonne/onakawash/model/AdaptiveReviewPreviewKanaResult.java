package com.yvonne.onakawash.model;


public class AdaptiveReviewPreviewKanaResult {
    //唯一标识哪个假名。
    //后面 service 会用它判断这个 kana 有没有可用 TangoItem。
    private String kanaItemId;
    //真正展示给用户看的假名。
    private String kana;
    //这个 kana 最近有多少条 AnswerRecord 证据。
    private int evidenceCount;
    //这个 kana 的弱项分数。
    //也是来自 BE-1。Preview API 不重新计算，只复用 BE-1 的结果
    private double weakScore;
    //这个 weak kana 现在有多少个 TangoItem 可以练。
    private long availableTangoItemCount;

    //因为不管这个 weak kana 有没有内容，它都需要一个统一的数据形状返回。
    public AdaptiveReviewPreviewKanaResult(
            String kanaItemId,
            String kana,
            int evidenceCount,
            double weakScore,
            long availableTangoItemCount
    ) {
        this.kanaItemId = kanaItemId;
        this.kana = kana;
        this.evidenceCount = evidenceCount;
        this.weakScore = weakScore;
        this.availableTangoItemCount = availableTangoItemCount;
    }
    //因为它主要是“返回结果”。service 创建它的时候一次性把值放进去，
    // 然后 Spring Boot 只需要通过 getter 把它变成 JSON。外面不需要再改它。

    public String getKanaItemId() {
        return kanaItemId;
    }

    public String getKana() {
        return kana;
    }

    public int getEvidenceCount() {
        return evidenceCount;
    }

    public double getWeakScore() {
        return weakScore;
    }

    public long getAvailableTangoItemCount() {
        return availableTangoItemCount;
    }
}
