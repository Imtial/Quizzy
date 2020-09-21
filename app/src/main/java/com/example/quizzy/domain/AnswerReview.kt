package com.example.quizzy.domain

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


class AnswerReview(submissions: List<Submission>?,
                   incorrect: List<String>?,
                   correct: List<String>?,
                   _id: String?,
                   quizId: String?,
                   userId: String?,
                   marks: Double?,
                   @field:SerializedName("createdAt") var createdAt: String,
                   @field:SerializedName("updatedAt") var updatedAt: String,
                   @field:SerializedName("rating") var rating: Double)
    : AnswerResponse(submissions, incorrect, correct, _id, quizId, userId, marks) {

    @get:Throws(Exception::class)
    val createDate: Date
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAt)

    @get:Throws(Exception::class)
    val updateDate: Date
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(updatedAt)

}