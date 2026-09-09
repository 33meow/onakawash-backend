package com.yvonne.onakawash.service;

import com.yvonne.onakawash.entity.ReviewAnswerRecordEntity;
import com.yvonne.onakawash.model.ReviewAnswerRequest;
import com.yvonne.onakawash.repository.PracticeSessionQuestionRepository;
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import com.yvonne.onakawash.repository.ReviewAnswerRecordRepository;
import com.yvonne.onakawash.repository.TangoItemRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReviewAnswerRecordService {

    private final PracticeSessionRepository sessionRepository;
    private final PracticeSessionQuestionRepository questionRepository;
    private final TangoItemRepository tangoItemRepository;
    private final ReviewAnswerRecordRepository answerRepository;

    public ReviewAnswerRecordService(
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

    @Transactional
    public ReviewAnswerRecordEntity saveAnswer(ReviewAnswerRequest request) {
        // 1. 检查提交的数据是否完整、格式是否合理。
        validateRequest(request);

        // 2. 查找这一轮练习，并检查会话类型。
        var session = sessionRepository.findBySessionKey(request.sessionKey())
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

        // 3. 从 BE-4 保存的题目序列中找到这一题。
        var question = questionRepository
                .findBySessionKeyOrderByQuestionIndexAsc(request.sessionKey())
                .stream()
                .filter(item -> request.questionIndex().equals(
                        item.getQuestionIndex()
                ))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Question does not belong to this session"
                ));

        if (!request.tangoItemId().equals(question.getTangoItemId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tango item does not match the assigned question"
            );
        }

        // 4. 已经保存过的题，不允许再次作答或覆盖。
        if (answerRepository.findBySessionKeyAndQuestionIndex(
                request.sessionKey(),
                request.questionIndex()
        ).isPresent()) {
            throw duplicateAnswer();
        }

        // 5. 从后端题库读取正确答案和可选项。
        var tangoItem = tangoItemRepository.findById(question.getTangoItemId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Assigned tango item not found"
                ));

        if (!tangoItem.getOptions().contains(request.selectedRomaji())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected answer must be one of the question options"
            );
        }

        // 这是服务端已有数据的问题，不归咎于用户提交。
        if (session.getUserId() == null
                || tangoItem.getCorrectRomaji() == null
                || !tangoItem.getOptions().contains(tangoItem.getCorrectRomaji())) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Session or question data is incomplete"
            );
        }

        // 6. 后端判分，再组装要保存的记录。
        boolean isCorrect = tangoItem.getCorrectRomaji()
                .equals(request.selectedRomaji());

        var answer = new ReviewAnswerRecordEntity(
                session.getUserId(),
                session.getSessionKey(),
                question.getQuestionIndex(),
                tangoItem.getTangoItemId(),
                request.selectedRomaji(),
                tangoItem.getCorrectRomaji(),
                isCorrect,
                request.answeredAt(),
                request.responseTimeMs()
        );

        // 7. 立即写入数据库，让唯一约束在这里检查重复。
        try {
            return answerRepository.saveAndFlush(answer);
        } catch (DataIntegrityViolationException exception) {
            // 两个请求同时到达时，可能都通过了第 4 步。
            // 数据库唯一约束会拦住第二次保存。
            if (isDuplicateAnswerConstraint(exception)) {
                throw duplicateAnswer();
            }
            // 其他数据库错误不冒充“重复提交”。
            throw exception;
        }
    }

    private void validateRequest(ReviewAnswerRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Request body is required"
            );
        }

        requireText(request.sessionKey(), "sessionKey");
        requireText(request.tangoItemId(), "tangoItemId");
        requireText(request.selectedRomaji(), "selectedRomaji");

        if (request.questionIndex() == null || request.questionIndex() < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "questionIndex must be at least 1"
            );
        }

        if (request.answeredAt() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "answeredAt is required"
            );
        }

        if (request.responseTimeMs() == null || request.responseTimeMs() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "responseTimeMs must be zero or greater"
            );
        }
    }

    private void requireText(String value, String fieldName) {
        if (value == null || value.isBlank() || value.length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must contain 1 to 255 characters"
            );
        }
    }

    private ResponseStatusException duplicateAnswer() {
        return new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This question already has a saved answer"
        );
    }

    private boolean isDuplicateAnswerConstraint(Throwable exception) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException violation) {
                String constraintName = violation.getConstraintName();

                if (constraintName != null) {
                    // H2 可能附带数据库前缀和 INDEX 说明。
                    // 先取空白之前的约束标识，并去掉双引号。
                    String identifier = constraintName
                            .trim()
                            .split("\\s+", 2)[0]
                            .replace("\"", "");

                    // PUBLIC.xxx → xxx
                    int lastDot = identifier.lastIndexOf('.');
                    String simpleName = identifier.substring(lastDot + 1);

                    if ("uk_review_answer_session_question"
                            .equalsIgnoreCase(simpleName)) {
                        return true;
                    }
                }
            }

            cause = cause.getCause();
        }

        return false;
    }
}