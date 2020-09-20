package com.example.quizzy.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quizzy.keyGen
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class QuestionResponse() : Question() {
    @SerializedName("_id")
    var questionId: String = keyGen()

//    constructor(description: String?, type: String?, options: List<String>?, answers: List<String>?) : this() {
//        this.description = description
//        this.type = type
//        this.options = options
//        this.answers = answers
//    }

    constructor(questionId: String, description: String, type: String?, options: List<String>?, marks: Float, answers: List<String>?)
            : this() {
        this.questionId = questionId
        this.description = description
        this.type = type
        this.options = options
        this.marks = marks
        this.answers = answers
    }

    constructor(questionId: String, question: Question): this() {
        this.questionId = questionId
        this.description = question.description
        this.type = question.type
        this.options = question.options
        this.marks = question.marks
        this.answers = question.answers
    }

    override fun toString(): String {
        return "QuestionResponse{" +
                "_id='" + questionId + '\'' +
                ", description='" + description + '\'' +
                ", answers=" + answers +
                ", options=" + options +
                ", type='" + type + '\'' +
                ", marks=" + marks +
                '}'
    }
}