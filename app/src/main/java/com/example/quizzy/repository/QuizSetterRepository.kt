package com.example.quizzy.repository

import android.util.Log
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.CachedQuiz
import com.example.quizzy.domain.Quiz
import com.example.quizzy.domain.QuizItem
import com.example.quizzy.domain.QuizResponse
import com.example.quizzy.network.NetworkQuizUtil
import com.example.quizzy.task.CreateQuizTask
import kotlinx.coroutines.*

class QuizSetterRepository(private val database: QuizDatabase, private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
    val liveToken = database.userDao.getLiveToken()
    private val networkQuizUtil = NetworkQuizUtil()

    fun insertQuiz(quiz: Quiz) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val token = database.userDao.getUserToken()
                Log.i("QUIZ-SET", "insertQuiz: $quiz $token")
                networkQuizUtil.postMyQuiz(token, quiz, object : CreateQuizTask {
                    override fun createQuiz(quizResponse: QuizResponse?) {
                        Log.i("POST-QUIZ", "createQuiz: $quizResponse")
                    }

                    override fun onFailure(msg: String?) {
                        Log.i("POST-QUIZ", "onFailure: $msg")
                    }
                })
            }
        }
    }
}