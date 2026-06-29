package com.yvonne.onakawash.controller;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.service.AnswerRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer-records")
@CrossOrigin(origins = "http://localhost:3000")
public class AnswerRecordController {
    private final AnswerRecordService answerRecordService;

    public AnswerRecordController(AnswerRecordService answerRecordService) {
        this.answerRecordService = answerRecordService;
    }

    //保存一条答题记录。
    @PostMapping
    public AnswerRecordEntity createAnswerRecord(@RequestBody AnswerRecordEntity answerRecord) {
        return answerRecordService.saveAnswerRecord(answerRecord);
    }

    //查看所有答题记录，开发测试用。
    @GetMapping
    public List<AnswerRecordEntity> getAllAnswerRecords() {
        return answerRecordService.getAllAnswerRecords();
    }

    //查看某一轮练习下面的所有答题记录。
    @GetMapping("/session/{sessionKey}")
    public List<AnswerRecordEntity> getAnswerRecordsBySessionKey(@PathVariable String sessionKey) {
        return answerRecordService.getAnswerRecordsBySessionKey(sessionKey);
    }
}
