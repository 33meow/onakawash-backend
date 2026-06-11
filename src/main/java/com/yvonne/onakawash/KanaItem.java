package com.yvonne.onakawash;

public class KanaItem {
    public String id;
    public String kana;
    public String romaji;
    public String audioSrc;
    public String imageSrc;

     public KanaItem(String id, String kana, String romaji, String audioSrc,String imageSrc){
         this.id = id;
         this.kana=kana;
         this.romaji=romaji;
         this.audioSrc=audioSrc;
         this.imageSrc = imageSrc;
     }
}
