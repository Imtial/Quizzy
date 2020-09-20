package com.example.quizzy.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.*
import com.example.quizzy.network.NetworkQuizUtil
import com.example.quizzy.task.ShowFeedTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuizRepository (private val database: QuizDatabase, private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
    suspend fun insertQuestion(question: Question) {
        database.questionDao.insert(question)
    }

    suspend fun updateQuestion(question: Question) {
        database.questionDao.update(question)
    }

    suspend fun deleteQuestion(question: Question) {
        database.questionDao.delete(question)
    }

    fun getQuestionList(): LiveData<List<Question>> = database.questionDao.getLiveQuestionList()

    fun getQuestionCount(): LiveData<Int> = database.questionDao.getLiveQuestionCount()

    fun getTotalMarks(): LiveData<Float> = database.questionDao.getLiveTotalMarks()

    suspend fun insertResponses(vararg respons: Response) {
        database.responseDao.insert(*respons)
    }

    fun getResponses() : LiveData<List<Response>> = database.responseDao.getLiveResponses()

//    suspend fun insertQuiz(cachedQuiz: CachedQuiz) {
//        cachedQuiz.questionResponses = database.questionDao.getQuestionList()
//        cachedQuiz.respons = database.responseDao.getResponses()
//        database.quizDao.insert(cachedQuiz)
//        database.questionDao.clearTable()
//        database.responseDao.clearTable()
//    }

    fun getQuiz(quizId: String) : LiveData<CachedQuiz?> = database.quizDao.getQuiz(quizId)

    suspend fun insertQuizItem(vararg quizItems: QuizItem) {
        database.quizItemDao.insert(*quizItems)
    }

    suspend fun clearQuizItemTable() {
        database.quizItemDao.clearTable()
    }

    val liveQuizItemList = database.quizItemDao.getLiveQuizItemList()

    private suspend fun insertUser(user: CachedUser) {
        database.userDao.insert(user)
    }

    suspend fun clearUserInfoTable() {
        database.userDao.clearTable()
    }

    val currentUser = database.userDao.getLiveCachedUser()

    private fun generateImageUrl(id: String): String {
        val baseUrl = "https://contest-quiz-app.herokuapp.com/users/"
        val queryKey = "/avatar"
        return baseUrl+id+queryKey
    }
    private val networkQuizUtil = NetworkQuizUtil()

    fun fetchQuizList(skip: Int = 0) {
        Log.i("FETCH-QUIZ", "fetchQuizList: enter")
        CoroutineScope(Dispatchers.IO).launch {
            val token = database.userDao.getUserToken()
            val queryHash = hashMapOf<String, String>()
            val limit = 5
            Log.i("FETCH-QUIZ", "fetchQuizList: coroutine 1")
            networkQuizUtil.showTopFeedQuizzes(token, queryHash, skip, limit, object : ShowFeedTask {
                override fun showTopFeedQuizzes(feedList: MutableList<QuizFeed>) {
                    Log.i("FETCH-QUIZ", "showTopFeedQuizzes: $feedList")
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