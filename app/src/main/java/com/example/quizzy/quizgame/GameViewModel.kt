package com.example.quizzy.quizgame

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.domain.Question
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.AnswerResponse
import com.example.quizzy.domain.Response
import com.example.quizzy.domain.Submission
import com.example.quizzy.repository.GameRepository
import com.example.quizzy.repository.QuizRepository

class GameViewModel(private val application: Application, private val quizId: String) : ViewModel() {

    private val repository = GameRepository(QuizDatabase.getDatabase(application), viewModelScope)
    val quiz = repository.getQuiz(quizId)

    val answerResponse = repository.answerResponse

    val submissionStatus = repository.submissionStatus

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> get() = _question

    val answerMap = HashMap<String, List<String>>()

    fun setChoices(choices: List<String>) {
        val questionId = quiz.value?.questionResponses!![questionSerial].questionId
        answerMap[questionId] = choices
    }
    fun getChoices() : List<String>? {
        val questionId = quiz.value?.questionResponses!![questionSerial].questionId
        return answerMap[questionId]
    }

    var totalQuestions = 0
    var questionSerial = 0
    fun init() {
        totalQuestions = quiz.value?.questionResponses?.size!!
        _question.value = quiz.value?.questionResponses!![questionSerial]

        if (questionSerial+1 == totalQuestions) {
            _lastQuestion.value = true
        }
    }

    private val _lastQuestion = MutableLiveData<Boolean>()
    val lastQuestion: LiveData<Boolean> get() = _lastQuestion

    fun triggerNextQuestion() {
        if (questionSerial+1 < totalQuestions) {
            _question.value = quiz.value?.questionResponses!![++questionSerial]
        }
        // Next button pressed & last question is reached
        if (questionSerial+1 == totalQuestions) {
            _lastQuestion.value = true
        }
    }

    fun triggerPrevQuestion() {
        // Back button is pressed & we are currently in last question
        if (questionSerial+1 == totalQuestions) {
            _lastQuestion.value = false
        }
        if (questionSerial > 0) {
            _question.value = quiz.value?.questionResponses!![--questionSerial]
        }
    }

    private val _enabled = MutableLiveData<Boolean>()
    val enabled: LiveData<Boolean> get() = _enabled

    fun disableQuiz() {
        _enabled.value = false
        _question.value = quiz.value?.questionResponses!![questionSerial]
    }

    private val _correct = MutableLiveData<Int>()
    val correct: LiveData<Int> get() = _correct

    private val _wrong = MutableLiveData<Int>()
    val wrong: LiveData<Int> get() = _wrong

    private val _unanswered = MutableLiveData<Int>()
    val unanswered: LiveData<Int> get() = _unanswered

    private val _marks = MutableLiveData<Float>()
    val marks: LiveData<Float> get() = _marks

    private val _totalMarks = MutableLiveData<Float>()
    val totalMarks: LiveData<Float> get() = _totalMarks

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun fetchAnswers() {
        val submissions = mutableListOf<Submission>()
        answerMap.forEach { (id, choices) ->
            submissions.add(Submission(id, choices))
        }
        repository.submitAndRequestForResult(quizId, submissions)
    }

    fun calculateResult(answerResponse: AnswerResponse) {
        _correct.value = answerResponse.correct?.size
        _wrong.value = answerResponse.incorrect?.size
        _unanswered.value = quiz.value?.questionResponses?.size?.minus(_correct.value!!)?.minus(_wrong.value!!)
        _marks.value = answerResponse.marks?.toFloat()
        _totalMarks.value = quiz.value?.questionResponses?.map { it.marks }?.sum()
        _message.value = retrieveMessage(_marks.value!!, answerResponse.responses)
    }

    private fun retrieveMessage(marks: Float, responses: List<Response>?): String {
        return responses?.find { marks > it.low && marks <= it.high }?.message ?: ""
    }

    private var _rating = 0F
    fun submit(rating: Float) {
        _rating = rating
        if (answerResponse.value != null) {
            repository.submitRating(answerResponse.value!!.answerId!!, rating)
        }
    }

}