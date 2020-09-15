package com.example.quizzy.quizgame

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.database.Question
import com.example.quizzy.database.Quiz
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository
import kotlinx.coroutines.*

class GameViewModel : ViewModel() {
    private lateinit var repository: QuizRepository

    fun setUpRepository (application: Application) {
        repository = QuizRepository(QuizDatabase.getDatabase(application))
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
//
//    private val _quiz = MutableLiveData<Quiz>()
//    val quiz: LiveData<Quiz> get() = _quiz
//
//    fun fetchQuestionList(quizId: String) {
//        coroutineScope.launch {
//            val quiz = repository.getQuiz(quizId)
//            withContext(Dispatchers.Main) {
//                _quiz.value = quiz
//            }
//        }
//    }
var test = "Accessed"

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}