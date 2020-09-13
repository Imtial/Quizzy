package com.example.quizzy.quizsetter

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.R
import com.example.quizzy.database.*
import com.example.quizzy.databinding.FragmentQuestionSetterBinding
import kotlinx.coroutines.*

class QuestionSetterViewModel(private val application: Application): ViewModel() {

    private val _questionType = MutableLiveData<Int>()
    val questionType : LiveData<Int> get() = _questionType

    init {
        _questionType.value = R.id.radio_single
    }

    fun setQuestionType(typeId: Int) {
        _questionType.value = typeId
    }

    private val database = QuizDatabase.getDatabase(application)
    private val repository = QuizRepository(database)
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    var questionList = listOf<Question>()
    val questionListLive = repository.getQuestionList()

    fun insert(question: Question) {
        coroutineScope.launch {
            repository.insertQuestion(question)
        }
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}