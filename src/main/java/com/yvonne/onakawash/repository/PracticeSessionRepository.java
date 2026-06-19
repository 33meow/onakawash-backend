package com.yvonne.onakawash.repository;
//repository是去和数据库说话的人，这里spring data JPA帮我做掉了大量重复工作

import com.yvonne.onakawash.entity.PracticeSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//PracticeSessionEntity 的主键id类型是Long
public interface PracticeSessionRepository extends JpaRepository<PracticeSessionEntity,Long>{
}
