package com.example.quizzy.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.AnswerResponse
import com.example.quizzy.domain.AnswerReview
import com.example.quizzy.domain.CachedQuiz
import com.example.quizzy.domain.Submission
import com.example.quizzy.network.NetworkQuizUtil
import com.example.quizzy.network.Status
import com.example.quizzy.task.GetAnswerScriptTask
import com.example.quizzy.task.PostReviewTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GameRepository(private val database: QuizDatabase, private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {

    fun getQuiz(quizId: String) : LiveData<CachedQuiz?> = database.quizDao.getQuiz(quizId)

    private val networkQuizUtil = NetworkQuizUtil()

    private val _answerResponse = MutableLiveData<AnswerResponse>()
    val answerResponse : LiveData<AnswerResponse> get() = _answerResponse

    private val _submissionStatus = MutableLiveData<Status>()
    val submissionStatus : LiveData<Status> get() = _submissionStatus

    fun submitAndRequestForResult(quizId: String, submissions: List<Submission>) {
        Log.i("FETCH-ANSWER", "submitAndRequestForResult: $submissions")
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val token = database.userDao.getUserToken()
                networkQuizUtil.getAnswerScript(token, quizId, submissions, object : GetAnswerScriptTask {
                    override fun getAnswerScript(answerResponse: AnswerResponse?) {
                        Log.i("FETCH-ANSWER", "getAnswerScript: $answerResponse")
                        _answerResponse.value = answerResponse!!
                    }

                    override fun onFailure(msg: String?) {
                        Log.i("FETCH-ANSWER", "onFailure: $msg")
                        _submissionStatus.value = Status.FAILURE
                    }
                })
            }
        }
    }

    fun submitRating(answerId: String, rating: Float) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val token = database.userDao.getUserToken()
                networkQuizUtil.postRating(token, answerId, rating.toDouble(), object : PostReviewTask {
                    override fun postRating(answerReview: AnswerReview?) {
                        Log.i("RATING", "postRating: $answerReview")
                    }

                    override fun onFailure(msg: String?) {
                        Log.i("RATING", "onFailure: $msg")
                    }
                })
            }
        }
    }
}