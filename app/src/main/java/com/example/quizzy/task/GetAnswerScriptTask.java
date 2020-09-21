package com.example.quizzy.task;

import com.example.quizzy.domain.AnswerResponse;

public interface GetAnswerScriptTask {
    void getAnswerScript(AnswerResponse answerResponse);
    void onFailure(String msg);
}
