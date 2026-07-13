package com.yvonne.onakawash.controller;

import com.yvonne.onakawash.model.AdaptiveReviewPreviewResult;
import com.yvonne.onakawash.service.AdaptiveReviewPreviewService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//告诉 Spring Boot：这是 API controller，返回值直接变成 JSON。
@RestController
//允许前端 localhost:3000 调这个后端 API。
@CrossOrigin(origins = "http://localhost:3000")
public class AdaptiveReviewPreviewController {

    private final AdaptiveReviewPreviewService adaptiveReviewPreviewService;

    public AdaptiveReviewPreviewController(
            AdaptiveReviewPreviewService adaptiveReviewPreviewService
    ) {
        this.adaptiveReviewPreviewService = adaptiveReviewPreviewService;
    }

    //get :因为 preview 只是读取当前状态，不创建东西、不保存东西。
    //不用post：因为 POST 通常代表创建资源，比如创建 session、保存 answer record。
    @GetMapping("/adaptive-review/preview")
    //这个 API 最后返回的 JSON 形状。
    public AdaptiveReviewPreviewResult getAdaptiveReviewPreview() {

        return adaptiveReviewPreviewService.getPreview();
    }
}