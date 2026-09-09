package com.yvonne.onakawash.repository;

import com.yvonne.onakawash.entity.PracticeSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PracticeSessionRepository
        extends JpaRepository<PracticeSessionEntity, Long> {

    //给已有 Repository 增加查询能力，原来的保存功能仍由 JpaRepository 提供。
    // 根据前端提交的 sessionKey，查找对应的一轮练习。
    Optional<PracticeSessionEntity> findBySessionKey(String sessionKey);
}