package com.example.quizzy.quizsetter

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.R
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class QuestionSetterViewModel(private val application: Application): ViewModel() {

    private val _questionType = MutableLiveData<Int>()
    val questionType : LiveData<Int> get() = _questionType

    init {
        _questionType.value = R.id.radio_single
    }

    fun setQuestionType(typeId: Int) {
        _questionType.value = typeId
    }
//    private lateinit var repository: QuizRepository
//    fun setApplication(application: Application) {
//        repository = QuizRepository(QuizDatabase.getDatabase(application))
//    }
    private val database = QuizDatabase.getDatabase(application)
    private val repository = QuizRepository(database)
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val _question = MutableLiveData<Question?>()
    val question: LiveData<Question?> get() = _question

    fun setQuestionSerial(serial: Int) {
//        coroutineScope.launch {
//            _question.value = repository.getQuestion(serial)
//        }
        _question.value = Question(serial, "Hello", MULTIPLE, listOf("a", "b", "c"), 2F, listOf("b"))
    }

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