package com.example.quizzy.domain;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizFeed extends QuizResponse {
    @SerializedName("access")
    String access;
    @SerializedName("rating")
    Double rating;
    @SerializedName("difficulty")
    Double difficulty;

    public QuizFeed(String title, String owner, double duration, String _id, String startTime,
                    List<String> tags, String access, Double rating, Double difficulty) {
        super(title, owner, duration, _id, startTime, tags);
        this.access = access;
        this.rating = rating;
        this.difficulty = difficulty;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "QuizFeed{" +
                "access='" + access + '\'' +
                ", rating=" + rating +
                ", difficulty=" + difficulty +
                ", title='" + title + '\'' +
                ", password='" + password + '\'' +
                ", duration=" + duration +
                ", _id='" + quizId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", owner='" + owner + '\'' +
                ", questions=" + questions +
                ", tags=" + tags +
                '}';
    }
}