package com.yvonne.onakawash.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        //同一个 sessionKey + 同一个 questionIndex
        // 数据库最多允许一条记录
        name = "review_answer_records",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_review_answer_session_question",
                columnNames = {"session_key", "question_index"}
        )
)
public class ReviewAnswerRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    // 哪一轮复习。
    @Column(name = "session_key", nullable = false)
    private String sessionKey;

    // 这一轮的第几题，从 1 开始。
    @Column(name = "question_index", nullable = false)
    private Integer questionIndex;

    // 具体是哪道词汇题。
    @Column(nullable = false)
    private String tangoItemId;

    @Column(nullable = false)
    private String selectedRomaji;

    // 保存答题时的正确答案，由后端提供。
    @Column(nullable = false)
    private String correctRomaji;

    // 由后端计算，不直接接受前端的正误判断。
    @Column(nullable = false)
    private Boolean isCorrect;

    @Column(nullable = false)
    private LocalDateTime answeredAt;

    @Column(nullable = false)
    private Integer responseTimeMs;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // JPA 从数据库读取记录时需要无参数构造方法。
    protected ReviewAnswerRecordEntity() {
    }

    public ReviewAnswerRecordEntity(
            Long userId,
            String sessionKey,
            Integer questionIndex,
            String tangoItemId,
            String selectedRomaji,
            String correctRomaji,
            Boolean isCorrect,
            LocalDateTime answeredAt,
            Integer responseTimeMs
    ) {
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.questionIndex = questionIndex;
        this.tangoItemId = tangoItemId;
        this.selectedRomaji = selectedRomaji;
        this.correctRomaji = correctRomaji;
        this.isCorrect = isCorrect;
        this.answeredAt = answeredAt;
        this.responseTimeMs = responseTimeMs;
    }

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public Integer getQuestionIndex() {
        return questionIndex;
    }

    public String getTangoItemId() {
        return tangoItemId;
    }

    public String getSelectedRomaji() {
        return selectedRomaji;
    }

    public String getCorrectRomaji() {
        return correctRomaji;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public Integer getResponseTimeMs() {
        return responseTimeMs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}