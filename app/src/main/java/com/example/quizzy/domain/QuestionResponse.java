package com.example.quizzy.domain;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionResponse {
    @SerializedName("_id")
    String questionId;
    @SerializedName("description")
    String description;
    @SerializedName("answers")
    List<String> answers;
    @SerializedName("options")
    List<String> options;
    @SerializedName("type")
    String type;
    @SerializedName("marks")
    double marks;

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public QuestionResponse() {
    }

    public QuestionResponse(String description, List<String> answers, List<String> options, String type) {
        this.description = description;
        this.answers = answers;
        this.options = options;
        this.type = type;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "QuestionResponse{" +
                "_id='" + questionId + '\'' +
                ", description='" + description + '\'' +
                ", answers=" + answers +
                ", options=" + options +
                ", type='" + type + '\'' +
                ", marks=" + marks +
                '}';
    }
}
