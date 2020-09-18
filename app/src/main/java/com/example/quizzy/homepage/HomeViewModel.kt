package com.example.quizzy.homepage

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository
import com.example.quizzy.domain.QuizItem

class HomeViewModel(private val application: Application) : ViewModel() {
    private val repository = QuizRepository(QuizDatabase.getDatabase(application))

    val liveQuizItemList = repository.liveQuizItemList

    fun fetchQuizList() {
        repository.fetchQuizList()
    }

    private val _selectedQuizItem = MutableLiveData<QuizItem>()
    val selectedQuizItem: LiveData<QuizItem> get() = _selectedQuizItem

    fun selectQuiz(quizItem: QuizItem) {
        _selectedQuizItem.value = quizItem
    }

    val fetchedQuizId = repository.fetchedQuizId

    var navigateToQuizGame = true

    // TODO - password needed
    fun fetchSelectedQuiz(id: String, password: String) {
        repository.fetchSelectedQuiz(id, password)
    }
}