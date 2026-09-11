package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.ReviewAnswerRecordEntity;
import com.yvonne.onakawash.model.AdaptiveReviewSessionDetailResult;
import com.yvonne.onakawash.model.AdaptiveReviewSessionDetailResult.Question;
import com.yvonne.onakawash.model.AdaptiveReviewSessionDetailResult.SavedAnswer;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import com.yvonne.onakawash.repository.ReviewAnswerRecordRepository;
import com.yvonne.onakawash.repository.TangoItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdaptiveReviewSessionDetailService {

    private final PracticeSessionRepository sessionRepository;
    private final PracticeSessionQuestionRepository questionRepository;
    private final TangoItemRepository tangoItemRepository;
    private final ReviewAnswerRecordRepository answerRepository;

    public AdaptiveReviewSessionDetailService(
            PracticeSessionRepository sessionRepository,
            PracticeSessionQuestionRepository questionRepository,
            TangoItemRepository tangoItemRepository,
            ReviewAnswerRecordRepository answerRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.tangoItemRepository = tangoItemRepository;
        this.answerRepository = answerRepository;
    }

    // 只读取，不保存或修改数据。
    @Transactional(readOnly = true)
    public AdaptiveReviewSessionDetailResult getSessionDetail(String sessionKey) {
        if (sessionKey == null || sessionKey.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Session key is required"
            );
        }

        var session = sessionRepository.findBySessionKey(sessionKey)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Practice session not found"
                ));

        if (!"ADAPTIVE_REVIEW".equals(session.getSessionType())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Session must be ADAPTIVE_REVIEW"
            );
        }

        // 使用数据库保存的顺序，不重新选题。
        var savedQuestions =
                questionRepository.findBySessionKeyOrderByQuestionIndexAsc(
                        sessionKey
                );

        // 已创建的练习应当有完整的题目序列。
        if (savedQuestions.isEmpty()
                || session.getTotalQuestions() == null
                || savedQuestions.size() != session.getTotalQuestions()) {
            throw incompleteSession();
        }

        Map<Integer, ReviewAnswerRecordEntity> answersByIndex = new HashMap<>();

        for (var answer :
                answerRepository.findBySessionKeyOrderByQuestionIndexAsc(
                        sessionKey
                )) {
            answersByIndex.put(answer.getQuestionIndex(), answer);
        }

        List<Question> questions = new ArrayList<>();
        int expectedIndex = 1;

        for (var savedQuestion : savedQuestions) {
            if (!Integer.valueOf(expectedIndex).equals(
                    savedQuestion.getQuestionIndex()
            )) {
                throw incompleteSession();
            }

            var tangoItem = tangoItemRepository
                    .findById(savedQuestion.getTangoItemId())
                    .orElseThrow(
                            AdaptiveReviewSessionDetailService::incompleteSession
                    );

            var options = tangoItem.getOptions();

            if (tangoItem.getDisplayText() == null
                    || tangoItem.getDisplayText().isBlank()
                    || options == null
                    || options.size() != 4
                    || options.stream().anyMatch(
                    option -> option == null || option.isBlank()
            )
                    || options.stream().distinct().count() != 4
                    || tangoItem.getCorrectRomaji() == null
                    || !options.contains(tangoItem.getCorrectRomaji())) {
                throw incompleteSession();
            }

            SavedAnswer savedAnswer = null;
            var answer = answersByIndex.get(savedQuestion.getQuestionIndex());

            if (answer != null) {
                if (!savedQuestion.getTangoItemId().equals(
                        answer.getTangoItemId()
                )
                        || answer.getSelectedRomaji() == null
                        || answer.getCorrectRomaji() == null
                        || answer.getIsCorrect() == null) {
                    throw incompleteSession();
                }

                // 使用保存时的判题结果，不重新判分。
                savedAnswer = new SavedAnswer(
                        answer.getSelectedRomaji(),
                        answer.getCorrectRomaji(),
                        answer.getIsCorrect()
                );
            }

            questions.add(new Question(
                    savedQuestion.getQuestionIndex(),
                    tangoItem.getTangoItemId(),
                    tangoItem.getDisplayText(),
                    List.copyOf(options),
                    savedAnswer
            ));

            expectedIndex++;
        }

        return new AdaptiveReviewSessionDetailResult(
                sessionKey,
                questions.size(),
                List.copyOf(questions)
        );
    }

    private static ResponseStatusException incompleteSession() {
        return new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Practice session question data is incomplete"
        );
    }
}