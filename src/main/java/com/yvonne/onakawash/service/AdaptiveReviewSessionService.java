package com.yvonne.onakawash.service;

import com.yvonne.onakawash.model.AdaptiveReviewPreviewResult;
import com.yvonne.onakawash.model.AdaptiveReviewSessionResult;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import com.yvonne.onakawash.repository.TangoItemRepository;
import org.springframework.stereotype.Service;
import com.yvonne.onakawash.entity.TangoItemEntity;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewKanaResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdaptiveReviewSessionService {

    // 一轮 Adaptive Review 最多出 10 题。
    // 这对应 BE-4 的完成标准：Select at most 10 distinct TangoItem questions.
    private static final int MAX_QUESTION_COUNT = 10;

    // 新创建的 PracticeSession 会用这个 sessionType。
    // 它对应 BE-4 的完成标准：区分 KANA_PRACTICE 和 ADAPTIVE_REVIEW。
    private static final String ADAPTIVE_REVIEW_SESSION_TYPE = "ADAPTIVE_REVIEW";

    // 如果没有可用 TangoItem，就返回这个状态，并且不创建 PracticeSession。
    // 这对应 BE-4 的完成标准：Return no_available_tango_content without creating a PracticeSession.
    private static final String NO_AVAILABLE_TANGO_CONTENT_STATUS = "no_available_tango_content";

    // 如果成功创建 session，就返回这个状态。
    private static final String CREATED_STATUS = "created";

    // 复用 BE-3 的 preview 逻辑。
    // 创建 session 前要重新验证 weak kana 和可用 TangoItem。
    private final AdaptiveReviewPreviewService adaptiveReviewPreviewService;

    // 用来从 tango_items 表里取出真正要出题的 TangoItem。
    private final TangoItemRepository tangoItemRepository;

    // 用来保存 PracticeSession 主记录。
    private final PracticeSessionRepository practiceSessionRepository;

    // 用来保存这一轮 session 的固定题目顺序。
    private final PracticeSessionQuestionRepository practiceSessionQuestionRepository;

    public AdaptiveReviewSessionService(
            AdaptiveReviewPreviewService adaptiveReviewPreviewService,
            TangoItemRepository tangoItemRepository,
            PracticeSessionRepository practiceSessionRepository,
            PracticeSessionQuestionRepository practiceSessionQuestionRepository
    ) {
        this.adaptiveReviewPreviewService = adaptiveReviewPreviewService;
        this.tangoItemRepository = tangoItemRepository;
        this.practiceSessionRepository = practiceSessionRepository;
        this.practiceSessionQuestionRepository = practiceSessionQuestionRepository;
    }

public AdaptiveReviewSessionResult createAdaptiveReviewSession(){
    AdaptiveReviewPreviewResult preview=
            adaptiveReviewPreviewService.getPreview();

    if (NO_AVAILABLE_TANGO_CONTENT_STATUS.equals(preview.getPreviewStatus())) {
        return new AdaptiveReviewSessionResult(
                NO_AVAILABLE_TANGO_CONTENT_STATUS,
                null,
                null,
                0,
                java.util.List.of(),
                java.util.List.of(),
                java.util.List.of(),
                preview.getWeakKanaWithoutAvailableContent()
        );
    }

    //只放“真的有 TangoItem 可以练”的 weak kana id
    List<String> weakKanaItemIdsWithAvailableContent = new ArrayList<>();

    for (AdaptiveReviewPreviewKanaResult weakKana :
            preview.getWeakKanaWithAvailableContent()) {
        weakKanaItemIdsWithAvailableContent.add(weakKana.getKanaItemId());
    }

    List<TangoItemEntity> availableTangoItems =
            tangoItemRepository.findDistinctByCoveredKanaItemIds(
                    weakKanaItemIdsWithAvailableContent
            );

    List<TangoItemEntity> selectedTangoItems = new ArrayList<>();

    for (TangoItemEntity tangoItem : availableTangoItems) {
        if (selectedTangoItems.size() >= MAX_QUESTION_COUNT) {
            break;
        }

        selectedTangoItems.add(tangoItem);
    }

    return new AdaptiveReviewSessionResult(
            "draft",
            null,
            null,
            selectedTangoItems.size(),
            java.util.List.of(),
            java.util.List.of(),
            java.util.List.of(),
            java.util.List.of()
    );
}

}