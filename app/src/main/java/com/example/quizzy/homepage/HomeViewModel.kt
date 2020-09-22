package com.example.quizzy.homepage

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.PRIVATE
import com.example.quizzy.domain.PUBLIC
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

    fun setQuizItemAccessType(access: String) {
        repository.setQuizItemAccessType(access)
    }

    fun fetchSearchedQuizItems(query: String) {
        repository.fetchQuizList(0, query)
    }
}