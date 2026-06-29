package com.yvonne.onakawash.repository;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRecordRepository extends JpaRepository<AnswerRecordEntity,Long> {

        //查出某一轮练习下面的所有答题记录
        List<AnswerRecordEntity> findBySessionKey(String sessionkey);

        //查出 HIRAGANA 或 KATAKANA 的所有答题记录
        List<AnswerRecordEntity> findByPracticeType(String practiceType);

}
