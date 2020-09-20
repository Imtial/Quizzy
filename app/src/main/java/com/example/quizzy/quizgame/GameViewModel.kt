package com.example.quizzy.quizgame

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.domain.Question
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.repository.QuizRepository

class GameViewModel(private val application: Application, private val quizId: String) : ViewModel() {

    private val repository = QuizRepository(QuizDatabase.getDatabase(application))
    val quiz = repository.getQuiz(quizId)

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> get() = _question

    val answerMap = HashMap<Int, List<String>>()

    fun setChoices(choices: List<String>) {
        answerMap[questionSerial] = choices
    }
    fun getChoices() : List<String>? {
        return answerMap[questionSerial]
    }

    var totalQuestions = 0
    var questionSerial = 0
    fun init() {
        totalQuestions = quiz.value?.questionResponses?.size!!
        _question.value = quiz.value?.questionResponses!![questionSerial]
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

    // TODO - cache in local db
    fun finishQuiz() {
        _unanswered.value = totalQuestions - answerMap.size

        var correctCounter = 0
        var wrongCounter = 0
        var marks = 0F
        var totalMarks = 0F
        for ((index, question) in quiz.value?.questionResponses!!.withIndex()) {
            totalMarks  += question.marks
            var correctFlag = true
            val answers = answerMap[index]
            if (answers != null) {
                question.answers?.zip(answers) { provided, submitted ->
                    if (!provided.equals(submitted, true)) {
                        correctFlag = false
                    }
                }
                if (correctFlag) {
                    marks += question.marks
                    correctCounter++
                } else wrongCounter++
            }
        }
        for (response in quiz.value?.respons!!) {
            if (marks > response.low && marks <= response.high) _message.value = response.message
        }
        _correct.value = correctCounter
        _wrong.value = wrongCounter
        _marks.value = marks
        _totalMarks.value = totalMarks
    }

    private var _rating = 0F
    fun submit(rating: Float) {
        _rating = rating
        Log.i("QUIZ-GAME", "submit: $rating")
    }

}