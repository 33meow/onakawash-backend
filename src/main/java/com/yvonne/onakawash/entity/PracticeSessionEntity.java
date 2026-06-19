package com.yvonne.onakawash.entity;
//告诉 Spring Boot，数据库里将来要有一张表，专门保存“一轮练习记录”

//告诉SpringBoot这个Java类对应数据库表
import jakarta.persistence.Entity;
//让数据库自动生成id
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
//主键，每条记录的唯一编号
import jakarta.persistence.Id;
//在保存到数据库之前自动执行的一小段逻辑，计划用来让他自动填入createdAt
import jakarta.persistence.PrePersist;
//指定数据库表名
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
//PracticeSessionEntity这个Java类对应数据库里的practice_sessions表
@Table(name = "practice_sessions")
public class PracticeSessionEntity {
    //数据库自动生成的主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //用户id，现在可用1，未来接登陆系统
    private Long userId;
    //这一轮练习的唯一标志，未来可以用uuid
    private String sessionKey;

    private String practiceType;
    private String practiceMode;

    private Integer score;
    private Integer totalQuestions;
    private Integer accuracy;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Integer durationSeconds;

    private LocalDateTime createdAt;

    //no-args constructor
    public PracticeSessionEntity(){

    }

    //void 不返回东西
    @PrePersist
    public void prePersist(){
        if (createdAt == null){
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

    public  String getSessionKey(){
        return sessionKey;
    }
    public void setSessionKey(String sessionKey){
        this.sessionKey = sessionKey;
    }

    public String getPracticeType(){
        return practiceType;
    }

    public void setPracticeType(String practiceType){
        this.practiceType = practiceType;
    }

    public String getPracticeMode(){
        return practiceMode;
    }

    public void setPracticeMode(String practiceMode){
        this.practiceMode = practiceMode;
    }

    public Integer getScore(){
        return score;
    }

    public void setScore(Integer score){
        this.score = score;
    }

    public Integer getTotalQuestions(){
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions){
        this.totalQuestions = totalQuestions;
    }

    public Integer getAccuracy(){
        return accuracy;
    }

    public void setAccuracy(Integer accuracy){
        this.accuracy = accuracy;
    }

    public LocalDateTime getStartedAt(){
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt){
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt(){
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt){
        this.finishedAt = finishedAt;
    }

    public Integer getDurationSeconds(){
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds){
        this.durationSeconds = durationSeconds;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
}
