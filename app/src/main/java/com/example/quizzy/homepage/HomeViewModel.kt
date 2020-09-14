package com.example.quizzy.homepage

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository

class HomeViewModel(private val application: Application) : ViewModel() {
    private val repository = QuizRepository(QuizDatabase.getDatabase(application))

    val liveQuizItemList = repository.getLiveQuizItemList()
}