//这个文件写给1. KanaMasteryService
//2. Spring Boot

package com.yvonne.onakawash.model;

public class KanaMasteryResult {
    //字段本身被保护住了
    private String kanaItemId;
    private String kana;
    private int evidenceCount;
    private double weakScore;
    private String status;

    public KanaMasteryResult(
            String kanaItemId,
            String kana,
            int evidenceCount,
            double weakScore,
            String status
    ) {
        this.kanaItemId = kanaItemId;
        this.kana = kana;
        this.evidenceCount = evidenceCount;
        this.weakScore = weakScore;
        this.status = status;
    }
//只有getter所以外面只能读不能改
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

    public String getStatus() {
        return status;
    }
}
