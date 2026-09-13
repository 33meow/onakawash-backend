package com.yvonne.onakawash.controller;

import com.yvonne.onakawash.model.AdaptiveReviewSessionDetailResult;
import com.yvonne.onakawash.model.AdaptiveReviewSessionResult;
import com.yvonne.onakawash.service.AdaptiveReviewSessionDetailService;
import com.yvonne.onakawash.service.AdaptiveReviewSessionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AdaptiveReviewSessionController {

    private final AdaptiveReviewSessionService adaptiveReviewSessionService;
    private final AdaptiveReviewSessionDetailService sessionDetailService;

    public AdaptiveReviewSessionController(
            AdaptiveReviewSessionService adaptiveReviewSessionService,
            AdaptiveReviewSessionDetailService sessionDetailService
    ) {
        this.adaptiveReviewSessionService = adaptiveReviewSessionService;
        this.sessionDetailService = sessionDetailService;
    }

    // 创建新的一轮复习。
    @PostMapping("/adaptive-review/sessions")
    public AdaptiveReviewSessionResult createAdaptiveReviewSession() {
        return adaptiveReviewSessionService.createAdaptiveReviewSession();
    }

    // 读取已经创建的那一轮复习。
    @GetMapping("/adaptive-review/sessions/{sessionKey}")
    public AdaptiveReviewSessionDetailResult getSessionDetail(
            @PathVariable("sessionKey") String sessionKey
    ) {
        return sessionDetailService.getSessionDetail(sessionKey);
    }
}