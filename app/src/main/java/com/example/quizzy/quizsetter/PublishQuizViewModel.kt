package com.example.quizzy.quizsetter

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PublishQuizViewModel(private val application: Application) : ViewModel() {
    private val repository = QuizRepository(QuizDatabase.getDatabase(application))
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    val totalMarks = repository.getTotalMarks()
    val totalQuestions = repository.getQuestionCount()

    private val _tags = mutableListOf<String>()
    private val _tagList = MutableLiveData<MutableList<String>>()
    val tagList : LiveData<MutableList<String>> get() = _tagList

    private val _completionSignal = MutableLiveData<Boolean>()
    val completionSignal: LiveData<Boolean> get() = _completionSignal

    fun insert(quiz: Quiz) {
        quiz.tags = _tags
        coroutineScope.launch {
            repository.insertQuiz(quiz)
//            _completionSignal.value = true
        }
    }

    fun addTag(tag: String) {
        _tags.add(tag)
        _tagList.value = _tags
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}