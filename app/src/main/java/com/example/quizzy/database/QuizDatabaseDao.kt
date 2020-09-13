package com.example.quizzy.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quiz: Quiz)

    @Update
    fun update(quiz: Quiz)

    @Delete
    fun delete(quiz: Quiz)

    @Query("SELECT * FROM table_quiz")
    fun getQuizList(): LiveData<List<Quiz>>
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
    suspend fun clearDB()

    @Query("SELECT * FROM table_question WHERE serial = :serial")
    fun get(serial: Int): LiveData<Question>

    @Query("SELECT SUM(marks) FROM table_question")
    suspend fun getTotalMarks(): Float

    @Query("SELECT COUNT(*) FROM table_question")
    suspend fun getQuestionCount() : Int

    @Query("SELECT * FROM table_question ORDER BY serial ASC")
    fun getQuestionList(): LiveData<List<Question>>
}

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(response: Response)

    @Update
    fun update(response: Response)

    @Delete
    fun delete(response: Response)



    @Query("SELECT * FROM table_response")
    fun getResponses(): LiveData<List<Response>>
}