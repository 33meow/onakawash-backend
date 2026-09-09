package com.yvonne.onakawash.repository;

import com.yvonne.onakawash.entity.ReviewAnswerRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewAnswerRecordRepository
        extends JpaRepository<ReviewAnswerRecordEntity, Long> {

    // 查找某轮某题已经保存的答案。
    Optional<ReviewAnswerRecordEntity> findBySessionKeyAndQuestionIndex(
            String sessionKey,
            Integer questionIndex
    );

    // 按题目顺序读取一整轮的词汇答题记录。
    List<ReviewAnswerRecordEntity> findBySessionKeyOrderByQuestionIndexAsc(
            String sessionKey
    );
}