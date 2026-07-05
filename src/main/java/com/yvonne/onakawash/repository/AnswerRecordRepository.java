package com.yvonne.onakawash.repository;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnswerRecordRepository extends JpaRepository<AnswerRecordEntity,Long> {

        //查出某一轮练习下面的所有答题记录
        List<AnswerRecordEntity> findBySessionKey(String sessionkey);

        //查出 HIRAGANA 或 KATAKANA 的所有答题记录
        List<AnswerRecordEntity> findByPracticeType(String practiceType);

        //从 answer_records 表里，
        //找出 kanaItemId 等于传入值的答题记录，
        //按 answeredAt 从新到旧排序，
        //并且用 pageable 限制只取几条。
        //Pageable这张说明书告诉 repository：不要全部拿，只拿第几页、每页几条
        List<AnswerRecordEntity> findByKanaItemIdOrderByAnsweredAtDesc(
                String kanaItemId,
                Pageable pageable
        );

}
