package com.example.quizzy.task;

import com.example.quizzy.domain.QuizFeed;

import java.util.List;

public interface ShowFeedTask {
    void showTopFeedQuizzes(List<QuizFeed> quizzes);
    void onFailure(String msg);
}
