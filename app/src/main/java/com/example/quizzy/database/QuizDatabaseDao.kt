package com.example.quizzy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizzy.domain.*

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cachedQuiz: CachedQuiz)

    @Update
    suspend fun update(cachedQuiz: CachedQuiz)

    @Delete
    suspend fun delete(cachedQuiz: CachedQuiz)

    @Query("DELETE FROM table_quiz")
    suspend fun clearTable()

    @Query("SELECT * FROM table_quiz")
    fun getQuizList(): List<CachedQuiz>

    @Query("SELECT * FROM table_quiz")
    fun getLiveQuizList(): LiveData<List<CachedQuiz>>

    @Query("SELECT * FROM table_quiz WHERE id = :quizId")
    fun getQuiz(quizId: String): LiveData<CachedQuiz?>
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

    @Query("SELECT SUM(marks) FROM table_question")
    fun getLiveTotalMarks(): LiveData<Float>

    @Query("SELECT COUNT(*) FROM table_question")
    fun getLiveQuestionCount() : LiveData<Int>

    @Query("SELECT * FROM table_question")
    fun getQuestionList() : List<Question>

    @Query("SELECT * FROM table_question")
    fun getLiveQuestionList(): LiveData<List<Question>>
}

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg respons: Response)

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
    suspend fun insert(vararg quizItems: QuizItem)

    @Query("DELETE FROM table_quiz_item")
    suspend fun clearTable()

    @Query("SELECT * FROM table_quiz_item")
    fun getLiveQuizItemList() : LiveData<List<QuizItem>>

    @Query("SELECT * FROM table_quiz_item WHERE access LIKE 'PUBLIC'")
    fun getLivePublicQuizItemList() : LiveData<List<QuizItem>>

    @Query("SELECT * FROM table_quiz_item WHERE access LIKE 'PRIVATE'")
    fun getLivePrivateQuizItemList() : LiveData<List<QuizItem>>

    @Query("SELECT COUNT(*) FROM table_quiz_item")
    fun getLiveCount() : LiveData<Int>
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cachedUser: CachedUser)

    @Query("DELETE FROM table_user")
    suspend fun clearTable()

    @Query("SELECT * FROM table_user LIMIT 1")
    fun getLiveCachedUser() : LiveData<CachedUser>

    @Query("SELECT token FROM table_user LIMIT 1")
    suspend fun getUserToken(): String

    @Query("SELECT token FROM table_user LIMIT 1")
    fun getLiveToken(): LiveData<String>
}