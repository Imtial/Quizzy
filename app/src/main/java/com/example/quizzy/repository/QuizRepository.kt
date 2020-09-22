package com.example.quizzy.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.*
import com.example.quizzy.network.NetworkQuizUtil
import com.example.quizzy.task.ShowFeedTask
import com.example.quizzy.utils.ImageUtil.generateImageUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuizRepository (private val database: QuizDatabase, private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {

    suspend fun insertQuizItem(vararg quizItems: QuizItem) {
        database.quizItemDao.insert(*quizItems)
    }

    suspend fun clearQuizItemTable() {
        database.quizItemDao.clearTable()
    }

//    val liveQuizItemList = database.quizItemDao.getLiveQuizItemList()

    private val livePublicQuizItemList = database.quizItemDao.getLivePublicQuizItemList()
    private val livePrivateQuizItemList = database.quizItemDao.getLivePrivateQuizItemList()

    private var currentAccess = PUBLIC
    val liveQuizItemList = MediatorLiveData<List<QuizItem>>()
    
    init {
        liveQuizItemList.addSource(livePublicQuizItemList) {
            if (currentAccess == PUBLIC) it?.let { liveQuizItemList.value = it }
        }
        liveQuizItemList.addSource(livePrivateQuizItemList) {
            if (currentAccess == PRIVATE) it?.let { liveQuizItemList.value = it }
        }
    }

    fun setQuizItemAccessType(access: String) {
        currentAccess = access
        when(access) {
            PUBLIC -> livePublicQuizItemList.value?.let { liveQuizItemList.value = it }
            PRIVATE -> livePrivateQuizItemList.value?.let { liveQuizItemList.value = it }
            else -> throw IllegalArgumentException("Undefined access type")
        }
    }

    private val networkQuizUtil = NetworkQuizUtil()

    private val _endOfQuizList = MutableLiveData<Boolean>()
    val endOfQuizList: LiveData<Boolean> get() = _endOfQuizList

    fun fetchQuizList(skip: Int = 0) {
        Log.i("FETCH-QUIZ", "fetchQuizList: enter")
        CoroutineScope(Dispatchers.IO).launch {
            val token = database.userDao.getUserToken()
            val queryHash = hashMapOf<String, String>()
            val limit = 5
            val page = skip * limit
            networkQuizUtil.showTopFeedQuizzes(token, queryHash, page, limit, object : ShowFeedTask {
                override fun showTopFeedQuizzes(feedList: MutableList<QuizFeed>) {
                    Log.i("FETCH-QUIZ", "showTopFeedQuizzes: $feedList")
                    if (feedList.isEmpty()) {
                        _endOfQuizList.value = true
                        return
                    }
                    val quizItems: List<QuizItem> = feedList.map {
                        QuizItem(it.quizId, it.title, it.questionCount, 0F, it.startDate.time, it.duration.toInt(),
                                it.userCount, it.tags, it.difficulty.toFloat(), it.rating.toFloat(), it.access, it.ownerName, generateImageUrl(it.owner))
                    }
                    Log.i("FETCH-QUIZ", "fetchQuizList: $quizItems")
                    CoroutineScope(Dispatchers.IO).launch {
                        insertQuizItem(*quizItems.toTypedArray())
                    }
                }

                override fun onFailure(msg: String?) {
                    Log.i("FETCH-QUIZ", "onFailure: $msg")
                }
            })
        }
    }

    private val _fetchedQuizId = MutableLiveData<String>()
    val fetchedQuizId: LiveData<String> get() = _fetchedQuizId

    fun fetchSelectedQuiz(id: String, password: String = NOPASSWORD) {
        CoroutineScope(Dispatchers.IO).launch {
            val token = database.userDao.getUserToken()
            networkQuizUtil.getQuestionsForAQuiz(token, id, password) {questionPaper ->
                Log.i("QPAPER", "fetchQuizById: $questionPaper")
                val quiz = CachedQuiz(id = questionPaper._id,
                        title = questionPaper.title,
                        questionResponses = questionPaper.questions,
                        duration = questionPaper.duration.toInt(),
                        startTime = questionPaper.startDate.time
                )
                Log.i("QPAPER", "fetchQuizById: $quiz")
                CoroutineScope(Dispatchers.IO).launch {
                    database.quizDao.insert(quiz)
                }
                _fetchedQuizId.value = quiz.id
            }
        }
    }

}