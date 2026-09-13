package com.yvonne.onakawash.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// 随一轮练习保存，不是独立的词库条目。
@Embeddable
public class SessionKanaCoverage {

    @Column(name = "kana_item_id", nullable = false)
    private String kanaItemId;

    @Column(name = "kana_text", nullable = false)
    private String kana;

    // covered：本轮题目覆盖
    // deferred：有内容，但本轮未安排
    // no_content：没有对应内容
    @Column(name = "coverage_status", nullable = false)
    private String status;

    protected SessionKanaCoverage() {
    }

    public SessionKanaCoverage(
            String kanaItemId,
            String kana,
            String status
    ) {
        this.kanaItemId = kanaItemId;
        this.kana = kana;
        this.status = status;
    }

    public String getKanaItemId() {
        return kanaItemId;
    }

    public String getKana() {
        return kana;
    }

    public String getStatus() {
        return status;
    }
}