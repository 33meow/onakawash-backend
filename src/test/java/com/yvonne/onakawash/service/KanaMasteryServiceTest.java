package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.AnswerRecordEntity;
import com.yvonne.onakawash.model.KanaMasteryResult;
import com.yvonne.onakawash.repository.AnswerRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;

//启动一个测试用的 Spring Boot 环境
@SpringBootTest
public class KanaMasteryServiceTest {

    //让 Spring Boot 把 repository 塞进测试 class。
    //我们用它来准备测试数据。
    @Autowired
    private AnswerRecordRepository answerRecordRepository;

    //让 Spring Boot 把我们真正写的 service 塞进来。
    //我们测试的就是它。
    @Autowired
    private KanaMasteryService kanaMasteryService;

    @BeforeEach
    void cleanAnswerRecords() {
        answerRecordRepository.deleteAll();
    }
    //测试方法名的含义：当最近证据多数错误时，返回 weak
    @Test
    void returnsWeakWhenRecentEvidenceIsMostlyIncorrect() {
        //是在准备测试数据。相当于你刚刚手动 POST 三次。
        saveAnswerRecord("hiragana-a", false, 2500, LocalDateTime.of(2026, 7, 5, 21, 0));
        saveAnswerRecord("hiragana-a", false, 3100, LocalDateTime.of(2026, 7, 5, 21, 1));
        saveAnswerRecord("hiragana-a", false, 1800, LocalDateTime.of(2026, 7, 5, 21, 2));

        //调用真正的业务逻辑。
        List<KanaMasteryResult> results = kanaMasteryService.getKanaMasteryResults();

        KanaMasteryResult hiraganaAResult = null;

        //从所有结果里找到 hiragana-a 那一条。
        for (KanaMasteryResult result : results) {
            if ("hiragana-a".equals(result.getKanaItemId())) {
                hiraganaAResult = result;
            }
        }

        //这是断言。意思是：我期望 evidenceCount 是 3
        //我期望 status 是 weak
        //如果实际结果不是这样，测试就失败。
        assertEquals(3, hiraganaAResult.getEvidenceCount());
        assertEquals("weak", hiraganaAResult.getStatus());
        assertEquals(1, hiraganaAResult.getAvailableTangoItemCount());
    }

    private void saveAnswerRecord(
            String kanaItemId,
            boolean isCorrect,
            int responseTimeMs,
            LocalDateTime answeredAt
    ) {
        AnswerRecordEntity record = new AnswerRecordEntity();
        record.setPracticeType("HIRAGANA");
        record.setPracticeMode("ROMAJI_CHOICE");
        record.setKanaItemId(kanaItemId);
        record.setIsCorrect(isCorrect);
        record.setResponseTimeMs(responseTimeMs);
        record.setAnsweredAt(answeredAt);

        answerRecordRepository.save(record);
    }
}
