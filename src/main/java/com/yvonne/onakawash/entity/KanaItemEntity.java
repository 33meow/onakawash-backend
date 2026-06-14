package com.yvonne.onakawash.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


//Entity means this Java class is connected to a database table
@Entity
//@Table(name="kana_items") means this class uses the table called kana_items
@Table(name="kana_items")

public class KanaItemEntity {
    //id means this field is the primary key
    //The primary key is the field marked with @id
    //A primary key is like an ID card number for each database row.
    //This field is the unique identifier of each row.
    @Id
    private String id;
    private String kana;
    private String romaji;
    private String audioSrc;
    private String imageSrc;
    private String type;
    private String section;
    //sectionOrder controls the order of sections.
    private Integer sectionOrder;
    //displayOrder controls the order of kana items inside one section.
    private Integer displayOrder;
    //the no-arg constructor is for JPA.
    public KanaItemEntity() {
    }
    public KanaItemEntity(String id, String kana, String romaji, String audioSrc, String imageSrc, String type,String section, Integer sectionOrder, Integer displayOrder){
        this.id = id;
        this.kana = kana;
        this.romaji=romaji;
        this.audioSrc=audioSrc;
        this.imageSrc=imageSrc;
        this.type=type;
        this.section=section;
        //sectionOrder and displayOrder are important because database rows do not automatically follow kana order.
        this.sectionOrder =sectionOrder;
        this.displayOrder = displayOrder;

    }
    //getters allow Service to read values from KanaItemEntity
    public String getId(){
        return id;
    }
    public String getKana(){
        return kana;
    }
    public String getRomaji(){
        return romaji;
    }
    public  String getAudioSrc(){
        return audioSrc;
    }
    public String getImageSrc(){
        return imageSrc;
    }
    public String getType(){
        return type;

    }
    public String getSection(){
        return section;
    }
    public Integer getSectionOrder(){
        return sectionOrder;
    }
    public Integer getDisplayOrder(){
        return displayOrder;
    }
}

