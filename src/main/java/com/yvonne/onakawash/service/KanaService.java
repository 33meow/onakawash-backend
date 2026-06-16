package com.yvonne.onakawash.service;


import com.yvonne.onakawash.legacy.Kana;
import com.yvonne.onakawash.model.KanaItem;
import com.yvonne.onakawash.model.KanaSection;
import org.springframework.stereotype.Service;

import com.yvonne.onakawash.entity.KanaItemEntity;
import com.yvonne.onakawash.repository.KanaItemRepository;

import java.util.ArrayList;
import java.util.List;
// @Service的意思 Spring Boot，请你管理这个类。
//以后 Controller 需要 KanaService 的时候，你自动帮它准备好。
@Service
public class KanaService {

    // Repository 是专门和数据库说话的对象。
    // Service 通过它从 H2 的 kana_items 表里读取数据。
    private final KanaItemRepository kanaItemRepository;

    // Constructor injection 构造器注入。
    // Spring Boot 会自动把 KanaItemRepository 塞进来。
    public KanaService(KanaItemRepository kanaItemRepository) {
        this.kanaItemRepository = kanaItemRepository;
    }

    // 这个方法专门负责准备 Hiragana 数据
    public List<KanaSection> getHiraganaSections() {
        List<KanaSection> sections = new ArrayList<>();

        sections.add(new KanaSection(
                "Basic Hiragana",
                "Basic hiragana sounds",
                getHiraganaBasicItems()
        ));
        sections.add(new KanaSection(
                "Dakuten Hiragana",
                "Hiragana with dakuten and handakuten sounds",
                getHiraganaDakutenItems()
        ));
        sections.add(new KanaSection(
                "Combination Hiragana",
                "Hiragana combination sounds",
                getHiraganaCombinationItems()
        ));
        return sections;}

    private List<KanaItem>getHiraganaBasicItems(){

        List<KanaItem> items = new ArrayList<>();
        items.add(new KanaItem("a", "あ", "a", "/audio/a.mp3", null));
        items.add(new KanaItem("i", "い", "i", "/audio/i.mp3", null));
        items.add(new KanaItem("u", "う", "u", "/audio/u.mp3", null));
        items.add(new KanaItem("e", "え", "e", "/audio/e.mp3", null));
        items.add(new KanaItem("o", "お", "o", "/audio/o.mp3", null));

        items.add(new KanaItem("ka", "か", "ka", "/audio/ka.mp3", null));
        items.add(new KanaItem("ki", "き", "ki", "/audio/ki.mp3", null));
        items.add(new KanaItem("ku", "く", "ku", "/audio/ku.mp3", null));
        items.add(new KanaItem("ke", "け", "ke", "/audio/ke.mp3", null));
        items.add(new KanaItem("ko", "こ", "ko", "/audio/ko.mp3", null));

        items.add(new KanaItem("sa", "さ", "sa", "/audio/sa.mp3", null));
        items.add(new KanaItem("si", "し", "si", "/audio/si.mp3", null));
        items.add(new KanaItem("su", "す", "su", "/audio/su.mp3", null));
        items.add(new KanaItem("se", "せ", "se", "/audio/se.mp3", null));
        items.add(new KanaItem("so", "そ", "so", "/audio/so.mp3", null));

        items.add(new KanaItem("ta", "た", "ta", "/audio/ta.mp3", null));
        items.add(new KanaItem("ti", "ち", "ti", "/audio/ti.mp3", null));
        items.add(new KanaItem("tu", "つ", "tu", "/audio/tu.mp3", null));
        items.add(new KanaItem("te", "て", "te", "/audio/te.mp3", null));
        items.add(new KanaItem("to", "と", "to", "/audio/to.mp3", null));

        items.add(new KanaItem("na", "な", "na", "/audio/na.mp3", null));
        items.add(new KanaItem("ni", "に", "ni", "/audio/ni.mp3", null));
        items.add(new KanaItem("nu", "ぬ", "nu", "/audio/nu.mp3", null));
        items.add(new KanaItem("ne", "ね", "ne", "/audio/ne.mp3", null));
        items.add(new KanaItem("no", "の", "no", "/audio/no.mp3", null));

        items.add(new KanaItem("ha", "は", "ha", "/audio/ha.mp3", null));
        items.add(new KanaItem("hi", "ひ", "hi", "/audio/hi.mp3", null));
        items.add(new KanaItem("hu", "ふ", "hu", "/audio/hu.mp3", null));
        items.add(new KanaItem("he", "へ", "he", "/audio/he.mp3", null));
        items.add(new KanaItem("ho", "ほ", "ho", "/audio/ho.mp3", null));

        items.add(new KanaItem("ma", "ま", "ma", "/audio/ma.mp3", null));
        items.add(new KanaItem("mi", "み", "mi", "/audio/mi.mp3", null));
        items.add(new KanaItem("mu", "む", "mu", "/audio/mu.mp3", null));
        items.add(new KanaItem("me", "め", "me", "/audio/me.mp3", null));
        items.add(new KanaItem("mo", "も", "mo", "/audio/mo.mp3", null));

        items.add(new KanaItem("ya", "や", "ya", "/audio/ya.mp3", null));
        items.add(null);
        items.add(new KanaItem("yu", "ゆ", "yu", "/audio/yu.mp3", null));
        items.add(null);
        items.add(new KanaItem("yo", "よ", "yo", "/audio/yo.mp3", null));

        items.add(new KanaItem("ra", "ら", "ra", "/audio/ra.mp3", null));
        items.add(new KanaItem("ri", "り", "ri", "/audio/ri.mp3", null));
        items.add(new KanaItem("ru", "る", "ru", "/audio/ru.mp3", null));
        items.add(new KanaItem("re", "れ", "re", "/audio/re.mp3", null));
        items.add(new KanaItem("ro", "ろ", "ro", "/audio/ro.mp3", null));

        items.add(new KanaItem("wa", "わ", "wa", "/audio/wa.mp3", null));
        items.add(null);
        items.add(null);
        items.add(null);
        items.add(new KanaItem("wo", "を", "wo", "/audio/wo.mp3", null));

        items.add(null);
        items.add(null);
        items.add(new KanaItem("n", "ん", "n", "/audio/n.mp3", null));
        items.add(null);
        items.add(null);
        return items;
    }
    private List<KanaItem> getHiraganaDakutenItems() {

        List<KanaItem> items = new ArrayList<>();

        items.add(new KanaItem("ga", "が", "ga", "/audio/ga.mp3", null));
        items.add(new KanaItem("gi", "ぎ", "gi", "/audio/gi.mp3", null));
        items.add(new KanaItem("gu", "ぐ", "gu", "/audio/gu.mp3", null));
        items.add(new KanaItem("ge", "げ", "ge", "/audio/ge.mp3", null));
        items.add(new KanaItem("go", "ご", "go", "/audio/go.mp3", null));

        items.add(new KanaItem("za", "ざ", "za", "/audio/za.mp3", null));
        items.add(new KanaItem("zi", "じ", "zi", "/audio/zi.mp3", null));
        items.add(new KanaItem("zu", "ず", "zu", "/audio/zu.mp3", null));
        items.add(new KanaItem("ze", "ぜ", "ze", "/audio/ze.mp3", null));
        items.add(new KanaItem("zo", "ぞ", "zo", "/audio/zo.mp3", null));

        items.add(new KanaItem("da", "だ", "da", "/audio/da.mp3", null));
        items.add(new KanaItem("di", "ぢ", "di", "/audio/di.mp3", null));
        items.add(new KanaItem("du", "づ", "du", "/audio/du.mp3", null));
        items.add(new KanaItem("de", "で", "de", "/audio/de.mp3", null));
        items.add(new KanaItem("do", "ど", "do", "/audio/do.mp3", null));

        items.add(new KanaItem("ba", "ば", "ba", "/audio/ba.mp3", null));
        items.add(new KanaItem("bi", "び", "bi", "/audio/bi.mp3", null));
        items.add(new KanaItem("bu", "ぶ", "bu", "/audio/bu.mp3", null));
        items.add(new KanaItem("be", "べ", "be", "/audio/be.mp3", null));
        items.add(new KanaItem("bo", "ぼ", "bo", "/audio/bo.mp3", null));

        items.add(new KanaItem("pa", "ぱ", "pa", "/audio/pa.mp3", null));
        items.add(new KanaItem("pi", "ぴ", "pi", "/audio/pi.mp3", null));
        items.add(new KanaItem("pu", "ぷ", "pu", "/audio/pu.mp3", null));
        items.add(new KanaItem("pe", "ぺ", "pe", "/audio/pe.mp3", null));
        items.add(new KanaItem("po", "ぽ", "po", "/audio/po.mp3", null));


        return items;
    }
    private List<KanaItem> getHiraganaCombinationItems() {

        List<KanaItem> items = new ArrayList<>();

        items.add(new KanaItem("kya", "きゃ", "kya", "/audio/kya.mp3", null));
        items.add(new KanaItem("kyu", "きゅ", "kyu", "/audio/kyu.mp3", null));
        items.add(new KanaItem("kyo", "きょ", "kyo", "/audio/kyo.mp3", null));

        items.add(new KanaItem("sya", "しゃ", "sya", "/audio/sya.mp3", null));
        items.add(new KanaItem("syu", "しゅ", "syu", "/audio/syu.mp3", null));
        items.add(new KanaItem("syo", "しょ", "syo", "/audio/syo.mp3", null));

        items.add(new KanaItem("tya", "ちゃ", "tya", "/audio/tya.mp3", null));
        items.add(new KanaItem("tyu", "ちゅ", "tyu", "/audio/tyu.mp3", null));
        items.add(new KanaItem("tyo", "ちょ", "tyo", "/audio/tyo.mp3", null));

        items.add(new KanaItem("nya", "にゃ", "nya", "/audio/nya.mp3", null));
        items.add(new KanaItem("nyu", "にゅ", "nyu", "/audio/nyu.mp3", null));
        items.add(new KanaItem("nyo", "にょ", "nyo", "/audio/nyo.mp3", null));

        items.add(new KanaItem("hya", "ひゃ", "hya", "/audio/hya.mp3", null));
        items.add(new KanaItem("hyu", "ひゅ", "hyu", "/audio/hyu.mp3", null));
        items.add(new KanaItem("hyo", "ひょ", "hyo", "/audio/hyo.mp3", null));

        items.add(new KanaItem("mya", "みゃ", "mya", "/audio/mya.mp3", null));
        items.add(new KanaItem("myu", "みゅ", "myu", "/audio/myu.mp3", null));
        items.add(new KanaItem("myo", "みょ", "myo", "/audio/myo.mp3", null));

        items.add(new KanaItem("rya", "りゃ", "rya", "/audio/rya.mp3", null));
        items.add(new KanaItem("ryu", "りゅ", "ryu", "/audio/ryu.mp3", null));
        items.add(new KanaItem("ryo", "りょ", "ryo", "/audio/ryo.mp3", null));

        items.add(new KanaItem("gya", "ぎゃ", "gya", "/audio/gya.mp3", null));
        items.add(new KanaItem("gyu", "ぎゅ", "gyu", "/audio/gyu.mp3", null));
        items.add(new KanaItem("gyo", "ぎょ", "gyo", "/audio/gyo.mp3", null));

        items.add(new KanaItem("zya", "じゃ", "zya", "/audio/zya.mp3", null));
        items.add(new KanaItem("zyu", "じゅ", "zyu", "/audio/zyu.mp3", null));
        items.add(new KanaItem("zyo", "じょ", "zyo", "/audio/zyo.mp3", null));

        items.add(new KanaItem("bya", "びゃ", "bya", "/audio/bya.mp3", null));
        items.add(new KanaItem("byu", "びゅ", "byu", "/audio/byu.mp3", null));
        items.add(new KanaItem("byo", "びょ", "byo", "/audio/byo.mp3", null));

        items.add(new KanaItem("pya", "ぴゃ", "pya", "/audio/pya.mp3", null));
        items.add(new KanaItem("pyu", "ぴゅ", "pyu", "/audio/pyu.mp3", null));
        items.add(new KanaItem("pyo", "ぴょ", "pyo", "/audio/pyo.mp3", null));


        return items;
    }

    public List<KanaSection> getKatakanaSections(){
        List<KanaSection> sections = new ArrayList<>();
        sections.add(new KanaSection(
                "Basic Katakana",
                "Basic katakana sounds",
                getKatakanaBasicItems()
        ));
        sections.add(new KanaSection(
                "Dakuten Katakana",
                "Katakana with dakuten and handakuten sounds",
getKatakanaDakutenItems()
        ));
        sections.add(new KanaSection(
                "Combination Katakana",
                "Katakana Combination sounds",
                getKatakanaCombinationItems()
        ));
return sections;
    }
    private List<KanaItem>getKatakanaBasicItems(){
        List<KanaItem> items = new ArrayList<>();
        items.add(new KanaItem("a", "ア", "a", "/audio/a.mp3", null));
        items.add(new KanaItem("i", "イ", "i", "/audio/i.mp3", null));
        items.add(new KanaItem("u", "ウ", "u", "/audio/u.mp3", null));
        items.add(new KanaItem("e", "エ", "e", "/audio/e.mp3", null));
        items.add(new KanaItem("o", "オ", "o", "/audio/o.mp3", null));

        items.add(new KanaItem("ka", "カ", "ka", "/audio/ka.mp3", null));
        items.add(new KanaItem("ki", "キ", "ki", "/audio/ki.mp3", null));
        items.add(new KanaItem("ku", "ク", "ku", "/audio/ku.mp3", null));
        items.add(new KanaItem("ke", "ケ", "ke", "/audio/ke.mp3", null));
        items.add(new KanaItem("ko", "コ", "ko", "/audio/ko.mp3", null));

        items.add(new KanaItem("sa", "サ", "sa", "/audio/sa.mp3", null));
        items.add(new KanaItem("si", "シ", "si", "/audio/si.mp3", null));
        items.add(new KanaItem("su", "ス", "su", "/audio/su.mp3", null));
        items.add(new KanaItem("se", "セ", "se", "/audio/se.mp3", null));
        items.add(new KanaItem("so", "ソ", "so", "/audio/so.mp3", null));

        items.add(new KanaItem("ta", "タ", "ta", "/audio/ta.mp3", null));
        items.add(new KanaItem("ti", "チ", "ti", "/audio/ti.mp3", null));
        items.add(new KanaItem("tu", "ツ", "tu", "/audio/tu.mp3", null));
        items.add(new KanaItem("te", "テ", "te", "/audio/te.mp3", null));
        items.add(new KanaItem("to", "ト", "to", "/audio/to.mp3", null));

        items.add(new KanaItem("na", "ナ", "na", "/audio/na.mp3", null));
        items.add(new KanaItem("ni", "ニ", "ni", "/audio/ni.mp3", null));
        items.add(new KanaItem("nu", "ヌ", "nu", "/audio/nu.mp3", null));
        items.add(new KanaItem("ne", "ネ", "ne", "/audio/ne.mp3", null));
        items.add(new KanaItem("no", "ノ", "no", "/audio/no.mp3", null));

        items.add(new KanaItem("ha", "ハ", "ha", "/audio/ha.mp3", null));
        items.add(new KanaItem("hi", "ヒ", "hi", "/audio/hi.mp3", null));
        items.add(new KanaItem("hu", "フ", "hu", "/audio/hu.mp3", null));
        items.add(new KanaItem("he", "ヘ", "he", "/audio/he.mp3", null));
        items.add(new KanaItem("ho", "ホ", "ho", "/audio/ho.mp3", null));

        items.add(new KanaItem("ma", "マ", "ma", "/audio/ma.mp3", null));
        items.add(new KanaItem("mi", "ミ", "mi", "/audio/mi.mp3", null));
        items.add(new KanaItem("mu", "ム", "mu", "/audio/mu.mp3", null));
        items.add(new KanaItem("me", "メ", "me", "/audio/me.mp3", null));
        items.add(new KanaItem("mo", "モ", "mo", "/audio/mo.mp3", null));

        items.add(new KanaItem("ya", "ヤ", "ya", "/audio/ya.mp3", null));
        items.add(null);
        items.add(new KanaItem("yu", "ユ", "yu", "/audio/yu.mp3", null));
        items.add(null);
        items.add(new KanaItem("yo", "ヨ", "yo", "/audio/yo.mp3", null));

        items.add(new KanaItem("ra", "ラ", "ra", "/audio/ra.mp3", null));
        items.add(new KanaItem("ri", "リ", "ri", "/audio/ri.mp3", null));
        items.add(new KanaItem("ru", "ル", "ru", "/audio/ru.mp3", null));
        items.add(new KanaItem("re", "レ", "re", "/audio/re.mp3", null));
        items.add(new KanaItem("ro", "ロ", "ro", "/audio/ro.mp3", null));

        items.add(new KanaItem("wa", "ワ", "wa", "/audio/wa.mp3", null));
        items.add(null);
        items.add(null);
        items.add(null);
        items.add(new KanaItem("wo", "ヲ", "wo", "/audio/wo.mp3", null));

        items.add(null);
        items.add(null);
        items.add(new KanaItem("n", "ン", "n", "/audio/n.mp3", null));
        items.add(null);
        items.add(null);

         return items;
       }

private List<KanaItem>getKatakanaDakutenItems(){
        List<KanaItem>items = new ArrayList<>();
    items.add(new KanaItem("ga", "ガ", "ga", "/audio/ga.mp3", null));
    items.add(new KanaItem("gi", "ギ", "gi", "/audio/gi.mp3", null));
    items.add(new KanaItem("gu", "グ", "gu", "/audio/gu.mp3", null));
    items.add(new KanaItem("ge", "ゲ", "ge", "/audio/ge.mp3", null));
    items.add(new KanaItem("go", "ゴ", "go", "/audio/go.mp3", null));

    items.add(new KanaItem("za", "ザ", "za", "/audio/za.mp3", null));
    items.add(new KanaItem("zi", "ジ", "zi", "/audio/zi.mp3", null));
    items.add(new KanaItem("zu", "ズ", "zu", "/audio/zu.mp3", null));
    items.add(new KanaItem("ze", "ゼ", "ze", "/audio/ze.mp3", null));
    items.add(new KanaItem("zo", "ゾ", "zo", "/audio/zo.mp3", null));

    items.add(new KanaItem("da", "ダ", "da", "/audio/da.mp3", null));
    items.add(new KanaItem("di", "ヂ", "di", "/audio/di.mp3", null));
    items.add(new KanaItem("du", "ヅ", "du", "/audio/du.mp3", null));
    items.add(new KanaItem("de", "デ", "de", "/audio/de.mp3", null));
    items.add(new KanaItem("do", "ド", "do", "/audio/do.mp3", null));

    items.add(new KanaItem("ba", "バ", "ba", "/audio/ba.mp3", null));
    items.add(new KanaItem("bi", "ビ", "bi", "/audio/bi.mp3", null));
    items.add(new KanaItem("bu", "ブ", "bu", "/audio/bu.mp3", null));
    items.add(new KanaItem("be", "ベ", "be", "/audio/be.mp3", null));
    items.add(new KanaItem("bo", "ボ", "bo", "/audio/bo.mp3", null));

    items.add(new KanaItem("pa", "パ", "pa", "/audio/pa.mp3", null));
    items.add(new KanaItem("pi", "ピ", "pi", "/audio/pi.mp3", null));
    items.add(new KanaItem("pu", "プ", "pu", "/audio/pu.mp3", null));
    items.add(new KanaItem("pe", "ペ", "pe", "/audio/pe.mp3", null));
    items.add(new KanaItem("po", "ポ", "po", "/audio/po.mp3", null));

    return items;
}
private List<KanaItem>getKatakanaCombinationItems(){
    List<KanaItem> items = new ArrayList<>();
    items.add(new KanaItem("kya", "キャ", "kya", "/audio/kya.mp3", null));
    items.add(new KanaItem("kyu", "キュ", "kyu", "/audio/kyu.mp3", null));
    items.add(new KanaItem("kyo", "キョ", "kyo", "/audio/kyo.mp3", null));

    items.add(new KanaItem("sya", "シャ", "sya", "/audio/sya.mp3", null));
    items.add(new KanaItem("syu", "シュ", "syu", "/audio/syu.mp3", null));
    items.add(new KanaItem("syo", "ショ", "syo", "/audio/syo.mp3", null));

    items.add(new KanaItem("tya", "チャ", "tya", "/audio/tya.mp3", null));
    items.add(new KanaItem("tyu", "チュ", "tyu", "/audio/tyu.mp3", null));
    items.add(new KanaItem("tyo", "チョ", "tyo", "/audio/tyo.mp3", null));

    items.add(new KanaItem("nya", "ニャ", "nya", "/audio/nya.mp3", null));
    items.add(new KanaItem("nyu", "ニュ", "nyu", "/audio/nyu.mp3", null));
    items.add(new KanaItem("nyo", "ニョ", "nyo", "/audio/nyo.mp3", null));

    items.add(new KanaItem("hya", "ヒャ", "hya", "/audio/hya.mp3", null));
    items.add(new KanaItem("hyu", "ヒュ", "hyu", "/audio/hyu.mp3", null));
    items.add(new KanaItem("hyo", "ヒョ", "hyo", "/audio/hyo.mp3", null));

    items.add(new KanaItem("mya", "ミャ", "mya", "/audio/mya.mp3", null));
    items.add(new KanaItem("myu", "ミュ", "myu", "/audio/myu.mp3", null));
    items.add(new KanaItem("myo", "ミョ", "myo", "/audio/myo.mp3", null));

    items.add(new KanaItem("rya", "リャ", "rya", "/audio/rya.mp3", null));
    items.add(new KanaItem("ryu", "リュ", "ryu", "/audio/ryu.mp3", null));
    items.add(new KanaItem("ryo", "リョ", "ryo", "/audio/ryo.mp3", null));

    items.add(new KanaItem("gya", "ギャ", "gya", "/audio/gya.mp3", null));
    items.add(new KanaItem("gyu", "ギュ", "gyu", "/audio/gyu.mp3", null));
    items.add(new KanaItem("gyo", "ギョ", "gyo", "/audio/gyo.mp3", null));

    items.add(new KanaItem("zya", "ジャ", "zya", "/audio/zya.mp3", null));
    items.add(new KanaItem("zyu", "ジュ", "zyu", "/audio/zyu.mp3", null));
    items.add(new KanaItem("zyo", "ジョ", "zyo", "/audio/zyo.mp3", null));

    items.add(new KanaItem("bya", "ビャ", "bya", "/audio/bya.mp3", null));
    items.add(new KanaItem("byu", "ビュ", "byu", "/audio/byu.mp3", null));
    items.add(new KanaItem("byo", "ビョ", "byo", "/audio/byo.mp3", null));

    items.add(new KanaItem("pya", "ピャ", "pya", "/audio/pya.mp3", null));
    items.add(new KanaItem("pyu", "ピュ", "pyu", "/audio/pyu.mp3", null));
    items.add(new KanaItem("pyo", "ピョ", "pyo", "/audio/pyo.mp3", null));
    return items;
}
    // 把数据库的一行 Entity 转成前端需要的 KanaItem。
// Entity = database row.
// KanaItem = frontend item.
    private KanaItem convertEntityToKanaItem(KanaItemEntity entity) {
        return new KanaItem(
                entity.getId(),
                entity.getKana(),
                entity.getRomaji(),
                entity.getAudioSrc(),
                entity.getImageSrc()
        );


    }
    private List<KanaItem> convertEntitiesToFixedSlots(
           //这个方法需要别人传进来一个 KanaItemEntity 列表，这个列表在方法里面叫 entities
            List<KanaItemEntity> entities,
            int totalSlots
    ){
        List<KanaItem> items = new ArrayList<>();

        for (int i=0;i<totalSlots;i++){
            //制造空位。现在数据库不存空位，所以 Service 要自己重新补空位。
            items.add(null);
        }
        //从 entities 这个列表里，一个一个拿出 KanaItemEntity，每次拿出来的这个东西，暂时叫 entity

        //entities = 一整筐数据库行
        //entity = 当前从筐里拿出来的这一行
        //displayOrder = 当前这一行里的位置编号
        for(KanaItemEntity entity: entities){
            //integer可以是null但是int不可
            //displayOrder 是你在这一行新定义的变量。
            Integer displayOrder = entity.getDisplayOrder();
            //做安全检查。
            if (displayOrder !=null&&displayOrder>=1&&displayOrder<=totalSlots){
                //Java 的 List 位置是从 0 开始
                int index = displayOrder -1;
                //把这个位置上的 null 替换成真正的 KanaItem。
                items.set(index,convertEntityToKanaItem(entity));
            }
        }
        return items;
    }
    // 这个方法返回前端需要的 Section 格式。
// 数据来源已经变成 H2 数据库。
    public List<KanaSection> getHiraganaSectionsFromDatabase() {
        List<KanaItemEntity> entities =
                kanaItemRepository.findByTypeOrderBySectionOrderAscDisplayOrderAsc("HIRAGANA");

        List<KanaItemEntity> basicEntities = new ArrayList<>();
        List<KanaItemEntity> dakutenEntities = new ArrayList<>();


        for (KanaItemEntity entity : entities) {
            if ("BASIC".equals(entity.getSection())) {
                basicEntities.add(entity);
            }
            if ("DAKUTEN".equals(entity.getSection())){
                dakutenEntities.add(entity);
            }
        }

        List<KanaItem> basicItems = convertEntitiesToFixedSlots(basicEntities, 55);

        List<KanaItem> dakutenItems = new ArrayList<>();
        for (KanaItemEntity entity:dakutenEntities){
            dakutenItems.add(convertEntityToKanaItem(entity));
        }
//又准备一个更大的空篮子。
//这个大篮子装 KanaSection。
        List<KanaSection> sections = new ArrayList<>();

        sections.add(new KanaSection(
                "Basic Hiragana",
                "Basic hiragana sounds",
                basicItems
        ));
        sections.add(new KanaSection(
                "Dakuten / Han-dakuten",
                "Voiced and semi-voiced hiragana sounds",
                dakutenItems
        ));

        return sections;
    }


}

