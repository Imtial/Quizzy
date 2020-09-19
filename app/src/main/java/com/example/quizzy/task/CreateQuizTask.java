package com.example.quizzy.task;


import com.example.quizzy.domain.QuizResponse;

public interface CreateQuizTask {
    void createQuiz(QuizResponse quizResponse);
    void onFailure(String msg);
}

