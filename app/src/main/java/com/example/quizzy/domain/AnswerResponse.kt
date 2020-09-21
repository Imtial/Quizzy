package com.example.quizzy.domain

import com.google.gson.annotations.SerializedName


open class AnswerResponse() {
    @SerializedName("submissions")
    var submissions: List<Submission>? = null

    @SerializedName("incorrect")
    var incorrect: List<String>? = null

    @SerializedName("correct")
    var correct: List<String>? = null

    @SerializedName("_id")
    var answerId: String? = null

    @SerializedName("quizId")
    var quizId: String? = null

    @SerializedName("userId")
    var userId: String? = null

    @SerializedName("marks")
    var marks: Double? = null

    @SerializedName("responses")
    var responses: List<Response>? = null

    constructor(submissions: List<Submission>?, incorrect: List<String>?, correct: List<String>?,
                answerId: String?, quizId: String?, userId: String?, marks: Double?, responses: List<Response>?): this() {
        this.submissions = submissions
        this.incorrect = incorrect
        this.correct = correct
        this.answerId = answerId
        this.quizId = quizId
        this.userId = userId
        this.marks = marks
        this.responses = responses
    }

    constructor(submissions: List<Submission>?, incorrect: List<String>?, correct: List<String>?,
                answerId: String?, quizId: String?, userId: String?, marks: Double?): this() {
        this.submissions = submissions
        this.incorrect = incorrect
        this.correct = correct
        this.answerId = answerId
        this.quizId = quizId
        this.userId = userId
        this.marks = marks
    }

    override fun toString(): String {
        return "AnswerResponse(submissions=$submissions, incorrect=$incorrect, correct=$correct, answerId=$answerId, quizId=$quizId, userId=$userId, marks=$marks)"
    }


}