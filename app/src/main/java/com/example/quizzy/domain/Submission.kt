package com.example.quizzy.domain

import com.google.gson.annotations.SerializedName

class Submission(
        @field:SerializedName("questionId") var questionId: String,
        @field:SerializedName("answers") var answers: List<String>
) {
    override fun toString(): String {
        return "Submission(questionId='$questionId', answers=$answers)"
    }
}