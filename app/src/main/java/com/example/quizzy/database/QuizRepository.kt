package com.example.quizzy.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.quizzy.domain.*
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

    suspend fun insertResponses(vararg responses: Response) {
        database.responseDao.insert(*responses)
    }

    fun getResponses() : LiveData<List<Response>> = database.responseDao.getLiveResponses()

    suspend fun insertQuiz(quiz: Quiz) {
        quiz.questions = database.questionDao.getQuestionList()
        quiz.responses = database.responseDao.getResponses()
        database.quizDao.insert(quiz)
//        database.quizDao.getQuizList().forEach {
//            Log.i("PUBLISH", "insertQuiz: $it")
//        }
        database.questionDao.clearTable()
        database.responseDao.clearTable()
    }

    fun getQuiz(quizId: String) : LiveData<Quiz> = database.quizDao.getQuiz(quizId)

    suspend fun insertQuizItem(quizItem: QuizItem) {
        database.quizItemDao.insert(quizItem)
    }

    suspend fun clearQuizItemTable() {
        database.quizItemDao.clearTable()
    }

    fun getLiveQuizItemList() : LiveData<List<QuizItem>> = database.quizItemDao.getLiveQuizItemList()

    suspend fun insertUser(user: CachedUser) {
        database.userDao.insert(user)
    }

    suspend fun clearUserInfoTable() {
        database.userDao.clearTable()
    }

    val currentUser = database.userDao.getLiveCachedUser()

    private val networkUtil = NetworkUtil()

    fun logInUser(email: String, password: String) {
        networkUtil.handleLogin(email, password) {responseUser ->
            val user = CachedUser(responseUser.userInfo.userId, responseUser.token, responseUser.userInfo.name, responseUser.userInfo.email)
            CoroutineScope(Dispatchers.IO).launch {
                insertUser(user)
            }
        }
    }
}