package com.example.quizzy.database

import android.util.Log
import androidx.lifecycle.LiveData


class QuizRepository (private val database: QuizDatabase) {
    suspend fun insertQuestion(question: Question) {
        database.questionDao.insert(question)
    }

    suspend fun updateQuestion(question: Question) {
        database.questionDao.update(question)
    }

    suspend fun deleteQuestion(question: Question) {
        database.questionDao.delete(question)
    }

    fun getQuestion(serial: Int): LiveData<Question> = database.questionDao.get(serial)

    fun getQuestionList(): LiveData<List<Question>> = database.questionDao.getQuestionList()
}