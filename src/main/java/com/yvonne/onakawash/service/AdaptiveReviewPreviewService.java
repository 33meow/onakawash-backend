package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.TangoItemEntity;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewKanaResult;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewResult;
import com.yvonne.onakawash.model.KanaMasteryResult;
import com.yvonne.onakawash.repository.TangoItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdaptiveReviewPreviewService {

    //一轮 Adaptive Review 最多 10 题。
    //现在只是 preview，所以它用来算理论题数，不创建 session。
    private static final int MAX_QUESTION_COUNT = 10;

    //复用 BE-1 / BE-2 的结果。
    //这样 BE-3 不重新计算 weakScore，也不重新判断 status。
    private final KanaMasteryService kanaMasteryService;
    //用来查一批 weak kana 对应的不重复 TangoItem。
    //它服务于 distinctAvailableTangoItemCount。
    private final TangoItemRepository tangoItemRepository;

    public AdaptiveReviewPreviewService(
            KanaMasteryService kanaMasteryService,
            TangoItemRepository tangoItemRepository
    ) {
        this.kanaMasteryService = kanaMasteryService;
        this.tangoItemRepository = tangoItemRepository;
    }

    public AdaptiveReviewPreviewResult getPreview() {
        List<KanaMasteryResult> kanaMasteryResults =
                kanaMasteryService.getKanaMasteryResults();

        //没有可用 TangoItem 的 weak kana。
        List<AdaptiveReviewPreviewKanaResult> weakKanaWithAvailableContent = new ArrayList<>();
        List<AdaptiveReviewPreviewKanaResult> weakKanaWithoutAvailableContent = new ArrayList<>();
        //所有 weak kana 的 id 列表。
        //这个列表后面会交给 repository 查可用 TangoItem。
        List<String> weakKanaItemIds = new ArrayList<>();

        for (KanaMasteryResult kanaMasteryResult : kanaMasteryResults) {
            //意思是：如果不是 weak，就跳过这次循环，不进入下面逻辑。
            //因为 BE-3 preview 只关心 weak kana。
            if (!"weak".equals(kanaMasteryResult.getStatus())) {
                continue;
            }

            weakKanaItemIds.add(kanaMasteryResult.getKanaItemId());

            AdaptiveReviewPreviewKanaResult previewKanaResult =
                    new AdaptiveReviewPreviewKanaResult(
                            kanaMasteryResult.getKanaItemId(),
                            kanaMasteryResult.getKana(),
                            kanaMasteryResult.getEvidenceCount(),
                            kanaMasteryResult.getWeakScore(),
                            kanaMasteryResult.getAvailableTangoItemCount()
                    );

            if (kanaMasteryResult.getAvailableTangoItemCount() > 0) {
                weakKanaWithAvailableContent.add(previewKanaResult);
            } else {
                weakKanaWithoutAvailableContent.add(previewKanaResult);
            }
        }

        int weakKanaCount = weakKanaItemIds.size();

        //所有 weak kana 能匹配到的不重复 TangoItem 总数。
        long distinctAvailableTangoItemCount = 0;

        if (!weakKanaItemIds.isEmpty()) {
            List<TangoItemEntity> distinctAvailableTangoItems =
                    tangoItemRepository.findDistinctByCoveredKanaItemIds(weakKanaItemIds);

            distinctAvailableTangoItemCount = distinctAvailableTangoItems.size();
        }

        //理论上最多能出几题。
        int theoreticalQuestionCount =
                (int) Math.min(MAX_QUESTION_COUNT, distinctAvailableTangoItemCount);

        //preview 的整体状态。
        //三个值
        String previewStatus;

        if (weakKanaCount == 0) {
            previewStatus = "no_weak_kana";
        } else if (distinctAvailableTangoItemCount == 0) {
            previewStatus = "no_available_tango_content";
        } else {
            previewStatus = "ready";
        }

        return new AdaptiveReviewPreviewResult(
                previewStatus,
                weakKanaCount,
                distinctAvailableTangoItemCount,
                theoreticalQuestionCount,
                weakKanaWithAvailableContent,
                weakKanaWithoutAvailableContent
        );
    }
}