package com.example.quizzy.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizzy.domain.*
import com.example.quizzy.network.NetworkQuizUtil
import com.example.quizzy.network.NetworkUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuizRepository (private val database: QuizDatabase) {
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

    suspend fun insertResponses(vararg responses: CachedResponse) {
        database.responseDao.insert(*responses)
    }

    fun getResponses() : LiveData<List<CachedResponse>> = database.responseDao.getLiveResponses()

    suspend fun insertQuiz(cachedQuiz: CachedQuiz) {
        cachedQuiz.questions = database.questionDao.getQuestionList()
        cachedQuiz.responses = database.responseDao.getResponses()
        database.quizDao.insert(cachedQuiz)
//        database.quizDao.getQuizList().forEach {
//            Log.i("PUBLISH", "insertQuiz: $it")
//        }
        database.questionDao.clearTable()
        database.responseDao.clearTable()
    }

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

    private val networkUtil = NetworkUtil()

    fun logInUser(email: String, password: String) {
        networkUtil.handleLogin(email, password) {userResponse ->
            val user = CachedUser(userResponse.userInfo.userId, userResponse.token, userResponse.userInfo.name, userResponse.userInfo.email)
            CoroutineScope(Dispatchers.IO).launch {
                insertUser(user)
            }
        }
    }

    private val networkQuizUtil = NetworkQuizUtil()

    fun fetchQuizList() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = database.userDao.getUserToken()
            val queryHash = hashMapOf<String, String>()
            val skip = 0
            val limit = 10
            networkQuizUtil.showTopFeedQuizzes(token, queryHash, skip, limit) {feedList ->
                val quizItems : List<QuizItem> = feedList.map { quizFeed: QuizFeed? ->
                    QuizItem(quizFeed?.quizId!!, quizFeed.title, quizFeed.questionCount, 25F, quizFeed.startDate.time,
                            quizFeed.duration.toInt(), quizFeed.userCount, quizFeed.tags, quizFeed.difficulty.toFloat(), quizFeed.rating.toFloat(),
                            quizFeed.access, quizFeed.ownerName)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    insertQuizItem(*quizItems.toTypedArray())
                }
            }
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
                        questions = questionPaper.questions,
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