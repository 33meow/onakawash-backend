package com.yvonne.onakawash.controller;

import com.yvonne.onakawash.model.AdaptiveReviewSessionResult;
import com.yvonne.onakawash.service.AdaptiveReviewSessionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AdaptiveReviewSessionController {

    private final AdaptiveReviewSessionService adaptiveReviewSessionService;

    public AdaptiveReviewSessionController(
            AdaptiveReviewSessionService adaptiveReviewSessionService
    ) {
        this.adaptiveReviewSessionService = adaptiveReviewSessionService;
    }

    @PostMapping("/adaptive-review/sessions")
    public AdaptiveReviewSessionResult createAdaptiveReviewSession() {
        return adaptiveReviewSessionService.createAdaptiveReviewSession();
    }
}