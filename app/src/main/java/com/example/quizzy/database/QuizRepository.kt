package com.example.quizzy.database

import com.example.quizzy.quizsetter.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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

    suspend fun getQuestion(serial: Int): Question? = database.questionDao.get(serial)
}