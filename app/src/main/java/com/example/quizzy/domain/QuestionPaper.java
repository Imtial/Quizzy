package com.example.quizzy.domain;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuestionPaper {
    @SerializedName("title")
    String title;
    @SerializedName("duration")
    double duration;
    @SerializedName("_id")
    String _id;
    @SerializedName("startTime")
    String startTime;

    @SerializedName("questions")
    List<QuestionResponse> questions;

    public QuestionPaper(String title, double duration, String _id, String startTime, List<QuestionResponse> questions) {
        this.title = title;
        this.duration = duration;
        this._id = _id;
        this.startTime = startTime;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

    public Date getStartDate() throws Exception{
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
    }

    @Override
    public String toString() {
        return "QuestionPaper{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", _id='" + _id + '\'' +
                ", startTime='" + startTime + '\'' +
                ", questions=" + questions +
                '}';
    }
}
