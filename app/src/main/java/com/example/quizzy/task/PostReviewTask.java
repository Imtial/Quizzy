package com.example.quizzy.task;

import com.example.quizzy.domain.AnswerReview;

public interface PostReviewTask {
    void postRating(AnswerReview answerReview);
    void onFailure(String msg);
}
