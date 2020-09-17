package com.example.quizzy.domain;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuizResponse {
    @SerializedName("title")
    String title;
    @SerializedName("password")
    String password;
    @SerializedName("duration")
    double duration;
    @SerializedName("_id")
    String quizId;
    @SerializedName("startTime")
    String startTime;
    @SerializedName("createdAt")
    String createdAt;
    @SerializedName("updatedAt")
    String updatedAt;
    @SerializedName("owner")
    String owner;
    @SerializedName("questions")
    List<QuestionResponse> questions;
    @SerializedName("tags")
    List<String> tags;

    public QuizResponse(String title, String password, double duration, String quizId,
                        String startTime, String createdAt, String updatedAt, String owner,
                        List<QuestionResponse> questions, List<String> tags) {
        this.title = title;
        this.password = password;
        this.duration = duration;
        this.quizId = quizId;
        this.startTime = startTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
        this.questions = questions;
        this.tags = tags;
    }

    public QuizResponse(String title, String owner, double duration, String quizId, String startTime, List<String> tags) {
        this.title = title;
        this.owner = owner;
        this.duration = duration;
        this.quizId = quizId;
        this.startTime = startTime;
        this.tags = tags;
    }

    public QuizResponse() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getStartDate() throws Exception{
        Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
        return startDate;
    }

    public Date getCreateDate() throws Exception{
        Date createDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAt);
        return createDate;
    }

    public Date getUpdateDate() throws Exception{
        Date updateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(updatedAt);
        return updateDate;
    }
}

