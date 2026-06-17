package com.yvonne.onakawash.service;



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

    private static final int BASIC_KANA_GRID_SLOTS = 55;

    // Repository 是专门和数据库说话的对象。
    // Service 通过它从 H2 的 kana_items 表里读取数据。
    private final KanaItemRepository kanaItemRepository;

    // Constructor injection 构造器注入。
    // Spring Boot 会自动把 KanaItemRepository 塞进来。
    public KanaService(KanaItemRepository kanaItemRepository) {

        this.kanaItemRepository = kanaItemRepository;
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
   private List<KanaItem> convertEntitiesToFixedSlots(List<KanaItemEntity> entities) {
        List<KanaItem> items = new ArrayList<>();

        for (int i=0;i<BASIC_KANA_GRID_SLOTS;i++){
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
            if (displayOrder !=null&&displayOrder>=1&&displayOrder<=BASIC_KANA_GRID_SLOTS){
                //Java 的 List 位置是从 0 开始
                int index = displayOrder -1;
                //把这个位置上的 null 替换成真正的 KanaItem。
                items.set(index,convertEntityToKanaItem(entity));
            }
        }
        return items;
    }

    private List<KanaSection> getKanaSectionsFromDatabase(
            String type,
            String basicTitle,
            String basicDescription,
            String dakutenTitle,
            String dakutenDescription,
            String combinationTitle,
            String combinationDescription
    ) {
        // 这里放原来两个方法里重复的查询、分组、转换、组装逻辑
        // 1. 从数据库查出所有 HIRAGANA 数据
        // Repository 已经按照 section_order 和 display_order 排好顺序了
        List<KanaItemEntity> entities =
                kanaItemRepository.findByTypeOrderBySectionOrderAscDisplayOrderAsc(type);
        // 2. 准备三个小篮子，分别装三个 section 的数据
        List<KanaItemEntity> basicEntities = new ArrayList<>();
        List<KanaItemEntity> dakutenEntities = new ArrayList<>();
        List<KanaItemEntity> combinationEntities = new ArrayList<>();

        // 3. 把数据库查出来的一长串 Hiragana 数据，按 section 分开
        for (KanaItemEntity entity : entities) {
            if ("BASIC".equals(entity.getSection())) {
                basicEntities.add(entity);
            }
            if ("DAKUTEN".equals(entity.getSection())){
                dakutenEntities.add(entity);
            }
            if ("COMBINATION".equals(entity.getSection())) {
                combinationEntities.add(entity);
            }
        }
        // 4. Basic 需要补空位，所以用 fixed slots
        List<KanaItem> basicItems = convertEntitiesToFixedSlots(basicEntities);
        // 5. Dakuten 不需要补空位，普通转换就行
        List<KanaItem> dakutenItems = new ArrayList<>();
        for (KanaItemEntity entity:dakutenEntities){
            dakutenItems.add(convertEntityToKanaItem(entity));
        }
        // 6. Combination 也不需要补空位，普通转换
        // 它的显示顺序来自 data.sql 里的 display_order
        List<KanaItem> combinationItems = new ArrayList<>();
        for (KanaItemEntity entity : combinationEntities) {
            combinationItems.add(convertEntityToKanaItem(entity));
        }

//又准备一个更大的空篮子。
//这个大篮子装 KanaSection。
// 7. 准备最终返回给前端的大篮子
        List<KanaSection> sections = new ArrayList<>();

        sections.add(new KanaSection(
              basicTitle,
                basicDescription,
                basicItems
        ));
        sections.add(new KanaSection(
               dakutenTitle,
                dakutenDescription,
                dakutenItems
        ));
        sections.add(new KanaSection(
             combinationTitle,
                combinationDescription,
                combinationItems
        ));
        return sections;
    }
    // 这个方法返回前端需要的 Section 格式。
// 数据来源已经变成 H2 数据库。
    public List<KanaSection> getHiraganaSectionsFromDatabase() {
return getKanaSectionsFromDatabase(
        "HIRAGANA",
        "Basic Hiragana",
        "Basic hiragana sounds",
        "Dakuten / Han-dakuten Hiragana",
        "Voiced and semi-voiced hiragana sounds",
        "Combination Hiragana",
        "Combined hiragana sounds");
    }

    public List<KanaSection> getKatakanaSectionsFromDatabase() {
return getKanaSectionsFromDatabase(
        "KATAKANA",
        "Basic Katakana",
        "Basic katakana sounds",
        "Dakuten / Han-dakuten Katakana",
        "Voiced and semi-voiced katakana sounds",
        "Combination Katakana",
        "Combined katakana sounds");
    }

}

