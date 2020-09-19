package com.example.quizzy.repository

import android.util.Log
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.CachedQuiz
import com.example.quizzy.network.NetworkQuizUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class QuizSetterRepository(private val database: QuizDatabase, coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
    val liveToken = database.userDao.getLiveToken()
    private val networkQuizUtil = NetworkQuizUtil()

    suspend fun insertQuiz(quiz: CachedQuiz) {
        Log.i("QUIZ-SET", "insertQuiz: $quiz")
        database.quizDao.insert(quiz)
    }
}