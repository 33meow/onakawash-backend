package com.yvonne.onakawash.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "answer_records")
public class AnswerRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String sessionKey;

    private String practiceType;
    private String practiceMode;

    private String kanaItemId;
    private String kana;
    private String correctRomaji;
    private String selectedRomaji;
    private Boolean isCorrect;

    private LocalDateTime answeredAt;
    private Integer responseTimeMs;

    private LocalDateTime createdAt;

    public AnswerRecordEntity(){


    }

    @PrePersist
    public void prePersist(){
        if(createdAt==null){
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId(){
        return id;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }

    public String getPracticeMode() {
        return practiceMode;
    }

    public void setPracticeMode(String practiceMode) {
        this.practiceMode = practiceMode;
    }

    public String getKanaItemId() {
        return kanaItemId;
    }

    public void setKanaItemId(String kanaItemId) {
        this.kanaItemId = kanaItemId;
    }

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getCorrectRomaji() {
        return correctRomaji;
    }

    public void setCorrectRomaji(String correctRomaji) {
        this.correctRomaji = correctRomaji;
    }

    public String getSelectedRomaji() {
        return selectedRomaji;
    }

    public void setSelectedRomaji(String selectedRomaji) {
        this.selectedRomaji = selectedRomaji;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Integer getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Integer responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
