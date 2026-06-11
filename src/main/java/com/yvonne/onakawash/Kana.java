package com.yvonne.onakawash;

public class Kana {
   //一个 Kana 对象有 4 个字段。
    public String id;
    public String kana;
    public String romaji;
    public String audio;

    public Kana(String id,String kana , String romaji,String audio){
        this.id=id;
        this.kana = kana;
        this.romaji = romaji;
        this.audio=audio;
    }
}