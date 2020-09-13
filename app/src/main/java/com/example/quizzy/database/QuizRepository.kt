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

    fun getQuestionList(): LiveData<List<Question>> = database.questionDao.getLiveQuestionList()

    fun getQuestionCount(): LiveData<Int> = database.questionDao.getLiveQuestionCount()

    fun getTotalMarks(): LiveData<Float> = database.questionDao.getLiveTotalMarks()

    suspend fun insertResponses(vararg responses: Response) {
        database.responseDao.insert(*responses)
    }

    fun getResponses() : LiveData<List<Response>> = database.responseDao.getLiveResponses()

    suspend fun insertQuiz(quiz: Quiz) {
        quiz.questions = database.questionDao.getQuestionList()
        quiz.responses = database.responseDao.getResponses()
        database.quizDao.insert(quiz)
        database.questionDao.clearTable()
        database.responseDao.clearTable()
    }
}