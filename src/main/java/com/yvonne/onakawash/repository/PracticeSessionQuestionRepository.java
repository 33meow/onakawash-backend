package com.yvonne.onakawash.repository;

import com.yvonne.onakawash.entity.PracticeSessionQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeSessionQuestionRepository extends JpaRepository<PracticeSessionQuestionEntity, Long> {

    //找出 sessionKey 等于某个值的所有题目
    //并且按照 questionIndex 从小到大排序
    List<PracticeSessionQuestionEntity> findBySessionKeyOrderByQuestionIndexAsc(String sessionKey);
}