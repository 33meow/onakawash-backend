package com.yvonne.onakawash.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tango_items")
public class TangoItemEntity {

    @Id
    private String tangoItemId;

    //这是展示给用户看的日文内容
    private String displayText;
    private String scriptType;
    private String meaningZh;
    private String correctRomaji;
    //表示这条内容是什么题型/内容类型。
    private String contentType;

    @ElementCollection
    @CollectionTable(
            name = "tango_item_options",
            joinColumns = @JoinColumn(name = "tango_item_id")
    )
    @Column(name = "option_value")
    //这是选择题选项。
    private List<String> options = new ArrayList<>();

    //这是一个简单值列表，不是另一个 Entity
    @ElementCollection
    //这个 List 用哪张附属表存
    //所以这一个 Entity 实际会产生三张表
    //tango_items
    //tango_item_options
    //tango_item_covered_kana
    @CollectionTable(
            name = "tango_item_covered_kana",
            joinColumns = @JoinColumn(name = "tango_item_id")
    )
    @Column(name = "kana_item_id")
    //这个词汇内容覆盖了哪些 kana item
    private List<String> coveredKanaItemIds = new ArrayList<>();

    public TangoItemEntity() {
    }

    public TangoItemEntity(
            String tangoItemId,
            String displayText,
            String scriptType,
            String meaningZh,
            String correctRomaji,
            String contentType,
            List<String> options,
            List<String> coveredKanaItemIds
    ) {
        this.tangoItemId = tangoItemId;
        this.displayText = displayText;
        this.scriptType = scriptType;
        this.meaningZh = meaningZh;
        this.correctRomaji = correctRomaji;
        this.contentType = contentType;
        this.options = options;
        this.coveredKanaItemIds = coveredKanaItemIds;
    }

    public String getTangoItemId() {
        return tangoItemId;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getScriptType() {
        return scriptType;
    }

    public String getMeaningZh() {
        return meaningZh;
    }

    public String getCorrectRomaji() {
        return correctRomaji;
    }

    public String getContentType() {
        return contentType;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getCoveredKanaItemIds() {
        return coveredKanaItemIds;
    }
}
