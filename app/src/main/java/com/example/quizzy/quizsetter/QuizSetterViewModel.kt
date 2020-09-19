package com.example.quizzy.quizsetter

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.R
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.CachedQuiz
import com.example.quizzy.domain.Response
import com.example.quizzy.domain.Question
import com.example.quizzy.domain.Quiz
import com.example.quizzy.repository.QuizSetterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizSetterViewModel(private val application: Application): ViewModel() {
    private val repository = QuizSetterRepository(QuizDatabase.getDatabase(application), viewModelScope)

    // To access token for api
    val liveToken = repository.liveToken

    // Store set up questions
    var questionList: MutableList<Question>? = null

    fun setQuestion(index: Int, question: Question) {
        if (questionList == null) questionList = mutableListOf()
        if (index < questionList?.size!!) questionList!![index] = question
        else questionList!!.add(question)
    }

    // To trigger question type
    private val _questionType = MutableLiveData<Int>()
    val questionType : LiveData<Int> get() = _questionType

    init {
        _questionType.value = R.id.radio_single
    }

    fun setQuestionType(typeId: Int) {
        _questionType.value = typeId
    }

    fun getTotalQuestions(): Int = questionList?.size!!
    fun getTotalMarks(): Float = questionList?.toList()?.map { it.marks }?.sum() ?: 0F

    private var _responseList = listOf<Response>()
    fun setResponses(respons: List<Response>) {
        _responseList = respons
    }

    private val _tags = mutableListOf<String>()
    private val _tagList = MutableLiveData<MutableList<String>>()
    val tagList : LiveData<MutableList<String>> get() = _tagList

    fun addTag(tag: String) {
        _tags.add(tag)
        _tagList.value = _tags
    }

    fun insert(quiz: Quiz) {
        quiz.responses = _responseList
        quiz.questions = questionList!!
        quiz.tags = _tags
        Log.i("POST-QUIZ", "insert: $quiz")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertQuiz(quiz)
            }
        }
    }
}