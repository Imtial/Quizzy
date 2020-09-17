package com.example.quizzy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizzy.domain.*

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quiz: Quiz)

    @Update
    suspend fun update(quiz: Quiz)

    @Delete
    suspend fun delete(quiz: Quiz)

    @Query("SELECT * FROM table_quiz")
    fun getQuizList(): List<Quiz>

    @Query("SELECT * FROM table_quiz")
    fun getLiveQuizList(): LiveData<List<Quiz>>

    @Query("SELECT * FROM table_quiz WHERE id = :quizId")
    fun getQuiz(quizId: String): LiveData<Quiz>
}

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)

    @Query("DELETE FROM table_question")
    suspend fun clearTable()

    @Query("SELECT * FROM table_question WHERE serial = :serial")
    fun get(serial: Int): LiveData<Question>

    @Query("SELECT SUM(marks) FROM table_question")
    fun getLiveTotalMarks(): LiveData<Float>

    @Query("SELECT COUNT(*) FROM table_question")
    fun getLiveQuestionCount() : LiveData<Int>

    @Query("SELECT * FROM table_question ORDER BY serial ASC")
    fun getQuestionList() : List<Question>

    @Query("SELECT * FROM table_question ORDER BY serial ASC")
    fun getLiveQuestionList(): LiveData<List<Question>>
}

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg responses: Response)

    @Update
    fun update(response: Response)

    @Delete
    fun delete(response: Response)

    @Query("DELETE FROM table_response")
    suspend fun clearTable()

    @Query("SELECT * FROM table_response")
    fun getResponses() : List<Response>

    @Query("SELECT * FROM table_response")
    fun getLiveResponses(): LiveData<List<Response>>
}

@Dao
interface QuizItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quizItem: QuizItem)

    @Query("DELETE FROM table_quiz_item")
    suspend fun clearTable()

    @Query("SELECT * FROM table_quiz_item")
    fun getLiveQuizItemList() : LiveData<List<QuizItem>>
}

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(responseUser: ResponseUser)

    @Query("DELETE FROM table_user")
    suspend fun clearTable()

    @Query("SELECT * FROM table_user LIMIT 1")
    fun getLiveUserInfo() : LiveData<ResponseUser>
}