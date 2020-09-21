package com.example.quizzy.homepage

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.repository.QuizRepository
import com.example.quizzy.domain.QuizItem

class HomeViewModel(private val application: Application) : ViewModel() {
    private val repository = QuizRepository(QuizDatabase.getDatabase(application), viewModelScope)

    val liveQuizItemList = repository.liveQuizItemList
    val endOfQuizList = repository.endOfQuizList

    var fetchCount = 0
    fun fetchQuizList() {
        repository.fetchQuizList(fetchCount++)
    }

    private val _selectedQuizItem = MutableLiveData<QuizItem>()
    val selectedQuizItem: LiveData<QuizItem> get() = _selectedQuizItem

    fun selectQuiz(quizItem: QuizItem) {
        _selectedQuizItem.value = quizItem
    }

    val fetchedQuizId = repository.fetchedQuizId

    var navigateToQuizGame = true

    fun fetchSelectedQuiz(id: String, password: String) {
        repository.fetchSelectedQuiz(id, password)
    }
}