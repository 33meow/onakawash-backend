package com.yvonne.onakawash.controller;

import com.yvonne.onakawash.entity.ReviewAnswerRecordEntity;
import com.yvonne.onakawash.model.ReviewAnswerRequest;
import com.yvonne.onakawash.service.ReviewAnswerRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adaptive-review/answers")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewAnswerRecordController {

    private final ReviewAnswerRecordService reviewAnswerRecordService;

    public ReviewAnswerRecordController(
            ReviewAnswerRecordService reviewAnswerRecordService
    ) {
        this.reviewAnswerRecordService = reviewAnswerRecordService;
    }

    // 接收一题词汇复习答案，交给 Service 校验、判分和保存。
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewAnswerRecordEntity saveAnswer(
            @RequestBody ReviewAnswerRequest request
    ) {
        return reviewAnswerRecordService.saveAnswer(request);
    }
}