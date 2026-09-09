package com.yvonne.onakawash.model;

import java.time.LocalDateTime;

// 前端提交一题词汇复习答案时，需要提供的数据。
public record ReviewAnswerRequest(
        String sessionKey,
        Integer questionIndex,
        String tangoItemId,
        String selectedRomaji,
        LocalDateTime answeredAt,
        Integer responseTimeMs
) {
}