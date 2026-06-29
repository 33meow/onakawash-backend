package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.repository.AnswerRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerRecordService {

    //这个 service 需要用 repository 做数据库操作。
    private final AnswerRecordRepository answerRecordRepository;

    //这是构造函数。Spring Boot 会自动把 AnswerRecordRepository 塞进来。
    public AnswerRecordService(AnswerRecordRepository answerRecordRepository) {
        this.answerRecordRepository = answerRecordRepository;
    }

    //保存一条答题记录。
    public AnswerRecordEntity saveAnswerRecord(AnswerRecordEntity answerRecord) {
        return answerRecordRepository.save(answerRecord);
    }

    //查全部答题记录，方便开发阶段调试。
    public List<AnswerRecordEntity> getAllAnswerRecords() {
        return answerRecordRepository.findAll();
    }

    //查某一轮练习里的所有题目记录。
    public List<AnswerRecordEntity> getAnswerRecordsBySessionKey(String sessionKey) {
        return answerRecordRepository.findBySessionKey(sessionKey);
    }
}