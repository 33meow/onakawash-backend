package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.PracticeSessionEntity;
import com.yvonne.onakawash.entity.PracticeSessionQuestionEntity;
import com.yvonne.onakawash.entity.SessionKanaCoverage;
import com.yvonne.onakawash.entity.TangoItemEntity;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewKanaResult;
import com.yvonne.onakawash.model.AdaptiveReviewPreviewResult;
import com.yvonne.onakawash.model.AdaptiveReviewSessionQuestionResult;
import com.yvonne.onakawash.model.AdaptiveReviewSessionResult;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import com.yvonne.onakawash.repository.TangoItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AdaptiveReviewSessionService {

    private static final int MAX_QUESTION_COUNT = 10;

    private final AdaptiveReviewPreviewService previewService;
    private final TangoItemRepository tangoItemRepository;
    private final PracticeSessionRepository sessionRepository;
    private final PracticeSessionQuestionRepository questionRepository;

    public AdaptiveReviewSessionService(
            AdaptiveReviewPreviewService previewService,
            TangoItemRepository tangoItemRepository,
            PracticeSessionRepository sessionRepository,
            PracticeSessionQuestionRepository questionRepository
    ) {
        this.previewService = previewService;
        this.tangoItemRepository = tangoItemRepository;
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
    }

    // 练习、题目和覆盖记录一起保存；其中一步失败就一起回滚。
    @Transactional
    public AdaptiveReviewSessionResult createAdaptiveReviewSession() {
        var preview = previewService.getPreview();

        List<String> weakKanaIds = preview
                .getWeakKanaWithAvailableContent()
                .stream()
                .map(AdaptiveReviewPreviewKanaResult::getKanaItemId)
                .toList();

        // 没有可练的薄弱假名，不创建空练习。
        if (weakKanaIds.isEmpty()) {
            return unavailable(preview);
        }

        List<TangoItemEntity> selectedItems = tangoItemRepository
                .findDistinctByCoveredKanaItemIds(weakKanaIds)
                .stream()
                .sorted(Comparator.comparing(TangoItemEntity::getTangoItemId))
                .limit(MAX_QUESTION_COUNT)
                .toList();

        if (selectedItems.isEmpty()) {
            return unavailable(preview);
        }

        // 只统计本轮真正选中的题目包含的假名。
        Set<String> selectedKanaIds = new HashSet<>();

        for (var item : selectedItems) {
            selectedKanaIds.addAll(item.getCoveredKanaItemIds());
        }

        List<AdaptiveReviewPreviewKanaResult> covered = new ArrayList<>();
        List<AdaptiveReviewPreviewKanaResult> deferred = new ArrayList<>();
        List<SessionKanaCoverage> snapshot = new ArrayList<>();

        for (var kana : preview.getWeakKanaWithAvailableContent()) {
            boolean included = selectedKanaIds.contains(kana.getKanaItemId());

            if (included) {
                covered.add(kana);
            } else {
                deferred.add(kana);
            }

            snapshot.add(new SessionKanaCoverage(
                    kana.getKanaItemId(),
                    kana.getKana(),
                    included ? "covered" : "deferred"
            ));
        }

        for (var kana : preview.getWeakKanaWithoutAvailableContent()) {
            snapshot.add(new SessionKanaCoverage(
                    kana.getKanaItemId(),
                    kana.getKana(),
                    "no_content"
            ));
        }

        String sessionKey = UUID.randomUUID().toString();

        var session = new PracticeSessionEntity();
        session.setUserId(1L);
        session.setSessionKey(sessionKey);
        session.setSessionType("ADAPTIVE_REVIEW");
        session.setPracticeType("ADAPTIVE_REVIEW");
        session.setPracticeMode("ROMAJI_CHOICE");
        session.setTotalQuestions(selectedItems.size());
        session.captureKanaCoverage(snapshot);

        var savedSession = sessionRepository.save(session);

        List<AdaptiveReviewSessionQuestionResult> questions = new ArrayList<>();
        int index = 1;

        for (var item : selectedItems) {
            var question = new PracticeSessionQuestionEntity();
            question.setSessionKey(sessionKey);
            question.setTangoItemId(item.getTangoItemId());
            question.setQuestionIndex(index);
            questionRepository.save(question);

            questions.add(new AdaptiveReviewSessionQuestionResult(
                    index,
                    item.getTangoItemId()
            ));

            index++;
        }

        return new AdaptiveReviewSessionResult(
                "created",
                savedSession.getId(),
                sessionKey,
                selectedItems.size(),
                questions,
                covered,
                deferred,
                preview.getWeakKanaWithoutAvailableContent()
        );
    }

    private AdaptiveReviewSessionResult unavailable(
            AdaptiveReviewPreviewResult preview
    ) {
        return new AdaptiveReviewSessionResult(
                "no_available_tango_content",
                null,
                null,
                0,
                List.of(),
                List.of(),
                List.of(),
                preview.getWeakKanaWithoutAvailableContent()
        );
    }
}