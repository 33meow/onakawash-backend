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
        // 1. 从数据库查出所有 HIRAGANA 数据
        // Repository 已经按照 section_order 和 display_order 排好顺序了
        List<KanaItemEntity> entities =
                kanaItemRepository.findByTypeOrderBySectionOrderAscDisplayOrderAsc("HIRAGANA");
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
        List<KanaItem> basicItems = convertEntitiesToFixedSlots(basicEntities, 55);
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
                "Basic Hiragana",
                "Basic hiragana sounds",
                basicItems
        ));
        sections.add(new KanaSection(
                "Dakuten / Han-dakuten",
                "Voiced and semi-voiced hiragana sounds",
                dakutenItems
        ));
        sections.add(new KanaSection(
                "Combination Hiragana",
                "Combined hiragana sounds",
                combinationItems
        ));
        return sections;
    }

    public List<KanaSection> getKatakanaSectionsFromDatabase() {
        // 1. 从数据库查出所有 HIRAGANA 数据
        // Repository 已经按照 section_order 和 display_order 排好顺序了
        List<KanaItemEntity> entities =
                kanaItemRepository.findByTypeOrderBySectionOrderAscDisplayOrderAsc("KATAKANA");
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
        List<KanaItem> basicItems = convertEntitiesToFixedSlots(basicEntities, 55);
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
                "Basic Katakana",
                "Basic katakana sounds",
                basicItems
        ));
        sections.add(new KanaSection(
                "Dakuten / Han-dakuten",
                "Voiced and semi-voiced katakana sounds",
                dakutenItems
        ));
        sections.add(new KanaSection(
                "Combination Katakana",
                "Combined katakana sounds",
                combinationItems
        ));
        return sections;
    }

}

