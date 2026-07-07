//保存 一轮 Adaptive Review 里的固定题目顺序。
package com.yvonne.onakawash.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "practice_session_questions")
public class PracticeSessionQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 这道题属于哪一轮 PracticeSession。
    // 它会对应 PracticeSessionEntity 里的 sessionKey。
    private String sessionKey;

    // 这一题使用哪一个 TangoItem。
    // 这里只存 id，不把整个 TangoItem 复制进来。
    private String tangoItemId;

    // 这道题在本轮 session 里的顺序。
    // 这里用 1 开始：1 = 第一题，2 = 第二题。
    private Integer questionIndex;

    public PracticeSessionQuestionEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getTangoItemId() {
        return tangoItemId;
    }

    public void setTangoItemId(String tangoItemId) {
        this.tangoItemId = tangoItemId;
    }

    public Integer getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(Integer questionIndex) {
        this.questionIndex = questionIndex;
    }
}