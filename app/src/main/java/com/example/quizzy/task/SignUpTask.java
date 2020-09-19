package com.example.quizzy.task;

import com.example.quizzy.domain.UserResponse;

public interface SignUpTask {
    void signUp(UserResponse userResponse);
    void onFailure(String msg);
}
