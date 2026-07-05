package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.entity.KanaItemEntity;
import com.yvonne.onakawash.model.KanaMasteryResult;
import com.yvonne.onakawash.repository.AnswerRecordRepository;
import com.yvonne.onakawash.repository.KanaItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


    //第一类：Service 需要哪些工具

    //final 不能被改

    //去 answer_records 表拿答题记录
    //BE-1 的 weakScore 只能从 AnswerRecord 里算出来。
    private final AnswerRecordRepository answerRecordRepository;
    //去 kana_items 表拿假名资料
    //分析结果里需要 kanaItemId 和 kana，所以要拿假名基础资料。
    private final KanaItemRepository kanaItemRepository;

    //第二类：constructor injection

    //Spring Boot 创建 KanaMasteryService 的时候，
    //会自动把这两个 Repository 交给它。

    public KanaMasteryService(
            AnswerRecordRepository answerRecordRepository,
            KanaItemRepository kanaItemRepository
    ) {

        // this.answerRecordRepository 是这个 Service 自己保存的工具。
        // 右边的 answerRecordRepository 是 Spring Boot 传进来的工具。
        //左边：是 class 里的字段。
        //右边：是 constructor 参数。
        this.answerRecordRepository = answerRecordRepository;
        this.kanaItemRepository = kanaItemRepository;
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

            int evidenceCount = recentRecords.size();

            results.add(new KanaMasteryResult(
                    kanaItem.getId(),
                    kanaItem.getKana(),
                    evidenceCount,
                    0.0,
                    "insufficient_evidence"
            ));
        }

        return results;
    }
}
