package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.entity.KanaItemEntity;
import com.yvonne.onakawash.model.KanaMasteryResult;
import com.yvonne.onakawash.repository.AnswerRecordRepository;
import com.yvonne.onakawash.repository.KanaItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.yvonne.onakawash.repository.TangoItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class KanaMasteryService {

    //分析一个假名掌握情况时，只看这个假名最近的 10 条 AnswerRecord。
    private static final int RECENT_RECORD_LIMIT = 10;
    //至少要有 3 条答题记录，才敢判断 normal 或 weak。
    private static final int MINIMUM_EVIDENCE_COUNT = 3;
    // weakScore 达到这个分数，就判断为 weak。
    // 例如 threshold 是 0.6，那么 weakScore >= 0.6 时 status = weak。
    private static final double WEAK_SCORE_THRESHOLD = 0.6;


    // 答题时间超过这个毫秒数，就认为这次答题偏慢。
    // 3000 ms = 3 秒。慢不是错误，但说明这个 kana 可能还不够熟。
    private static final int SLOW_RESPONSE_THRESHOLD_MS = 3000;

    // weakScore 里，错误率占主要权重。
    // V0.6 规则要求 incorrect answers weighted more heavily.
    private static final double INCORRECT_WEIGHT = 0.8;

    // weakScore 里，慢速率占辅助权重。
    // 慢答会增加 weakScore，但影响比答错小。
    private static final double SLOW_WEIGHT = 0.2;

    //第一类：Service 需要哪些工具

    //final 不能被改

    //去 answer_records 表拿答题记录
    //BE-1 的 weakScore 只能从 AnswerRecord 里算出来。
    private final AnswerRecordRepository answerRecordRepository;
    //去 kana_items 表拿假名资料
    //分析结果里需要 kanaItemId 和 kana，所以要拿假名基础资料。
    private final KanaItemRepository kanaItemRepository;

    private final TangoItemRepository tangoItemRepository;

    //第二类：constructor injection

    //Spring Boot 创建 KanaMasteryService 的时候，
    //会自动把这两个 Repository 交给它。

    public KanaMasteryService(
            AnswerRecordRepository answerRecordRepository,
            KanaItemRepository kanaItemRepository,
            TangoItemRepository tangoItemRepository
    ) {

        // this.answerRecordRepository 是这个 Service 自己保存的工具。
        // 右边的 answerRecordRepository 是 Spring Boot 传进来的工具。
        //左边：是 class 里的字段。
        //右边：是 constructor 参数。
        this.answerRecordRepository = answerRecordRepository;
        this.kanaItemRepository = kanaItemRepository;
        this.tangoItemRepository = tangoItemRepository;
    }


    //第三类：主方法

    // TODO: 当前实现会对每个 kanaItemId 查询一次最近 AnswerRecord。
    // 这对 V0.6 的小数据量可以接受，但以后 kana 数量或用户数据变多时，
    // 可以优化成批量查询 AnswerRecord 后按 kanaItemId 分组，减少数据库查询次数。



    //这个方法会返回很多条 KanaMasteryResult。
    // 返回值是 List，因为每个 kana item 都会有一条 KanaMasteryResult。
    public List<KanaMasteryResult> getKanaMasteryResults() {
        List<KanaMasteryResult> results = new ArrayList<>();

        //Desc is short for Descending.
        // Here it means newer answeredAt records come first.
        //降序 / 从大到小 / 从新到旧
        List<KanaItemEntity> kanaItems = new ArrayList<>();
        kanaItems.addAll(kanaItemRepository.findByTypeOrderBySectionOrderAscDisplayOrderAsc("HIRAGANA"));
        kanaItems.addAll(kanaItemRepository.findByTypeOrderBySectionOrderAscDisplayOrderAsc("KATAKANA"));

        //最近记录数量固定为 10，并且集中放在这里。
        Pageable recentRecordsPage = PageRequest.of(0, RECENT_RECORD_LIMIT);

        for (KanaItemEntity kanaItem : kanaItems) {
            List<AnswerRecordEntity> recentRecords =
                    answerRecordRepository.findByKanaItemIdOrderByAnsweredAtDesc(
                            kanaItem.getId(),
                            recentRecordsPage
                    );

            //第一步：数证据数量
            int evidenceCount = recentRecords.size();

            //第二步：数错误次数和慢速次数（先从 0 开始。）
            int incorrectCount = 0;
            int slowCount = 0;

            for (AnswerRecordEntity record : recentRecords) {
                if (Boolean.FALSE.equals(record.getIsCorrect())) {
                    incorrectCount++;
                }

                if (
                        //第三步：判断慢
                        //有答题时间，并且超过 3000 ms（3000ms是上面定义的）
                        record.getResponseTimeMs() != null &&
                                record.getResponseTimeMs() > SLOW_RESPONSE_THRESHOLD_MS
                ) {
                    slowCount++;
                }
            }

            double recentIncorrectRate = 0.0;
            double recentSlowRate = 0.0;

            if (evidenceCount > 0) {
                //第四步：算比例
                //* 1.0 是为了让 Java 算小数。
                recentIncorrectRate = incorrectCount * 1.0 / evidenceCount;
                recentSlowRate = slowCount * 1.0 / evidenceCount;
            }

            //第五步：算 weakScore
            //weakScore = 错误率 * 0.8 + 慢速率 * 0.2
            double weakScore =
                    recentIncorrectRate * INCORRECT_WEIGHT +
                            recentSlowRate * SLOW_WEIGHT;

            String status;

            //第六步：判断 status

            if (evidenceCount < MINIMUM_EVIDENCE_COUNT) {
                status = "insufficient_evidence";
            } else if (weakScore >= WEAK_SCORE_THRESHOLD) {
                status = "weak";
            } else {
                status = "normal";
            }
            long availableTangoItemCount = 0;

            if ("weak".equals(status)) {
                availableTangoItemCount = tangoItemRepository.countByCoveredKanaItemId(kanaItem.getId());
            }

            results.add(new KanaMasteryResult(
                    kanaItem.getId(),
                    kanaItem.getKana(),
                    evidenceCount,
                    weakScore,
                    status,
                    availableTangoItemCount
            ));

        }

        return results;
    }

}
