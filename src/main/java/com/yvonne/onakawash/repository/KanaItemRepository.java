
//Repository 写成 interface，因为 Spring Data JPA 会自动帮你生成真正的实现代码。
//JpaRepository gives this Repository many ready-made database methods.
//JpaRepository 会给这个 Repository 很多现成的数据库方法
//An interface is usually a rule that another class follows.
package com.yvonne.onakawash.repository;


import com.yvonne.onakawash.entity.KanaItemEntity;
import  org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KanaItemRepository extends JpaRepository<KanaItemEntity, String>{
    //String 告诉 JpaRepository 主键id的类型。
    //findByType means: find rows where the type field （字段）matches the given value.
    //findByType 的意思是：查找 type 字段等于传入值的数据。
    //OrderBySectionOrderAsc means: sort by sectionOrder from small to large.
    //OrderBySectionOrderAsc 的意思是：按照 sectionOrder 从小到大排序。
    //display那个同理
    //find kana items by type, then sort them by section order and display order.

   //findBy is a Spring Data JPA keyword.
    //Type refers to the field named type in KanaItemEntity.
   //OrderBy is another Spring Data JPA keyword.
    //SectionOrder refers to the field named sectionOrder
    //Asc meas ascending order, from small to large
    List<KanaItemEntity>findByTypeOrderBySectionOrderAscDisplayOrderAsc(String type);
}
