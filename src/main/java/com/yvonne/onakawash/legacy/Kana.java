//legacy test class
package com.yvonne.onakawash.legacy;

public class Kana {
   //一个 Kana 对象有 4 个字段。
    public String id;
    public String kana;
    public String romaji;
    public String audioSrc;

    public Kana(String id,String kana , String romaji,String audioSrc){
        this.id=id;
        this.kana = kana;
        this.romaji = romaji;
        this.audioSrc=audioSrc;
    }
}