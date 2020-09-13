package com.example.quizzy.database

import androidx.room.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

const val SINGLE = 1
const val MULTIPLE = 2
const val TYPED = 3

@Entity(tableName = "table_quiz")
data class Quiz(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val title: String = "untitled",
        @TypeConverters(QuestionsConverter::class)
        val questions: List<Question>,
        @TypeConverters(ResponsesConverter::class)
        val responses: List<Response>,
        @TypeConverters(ListConverter::class)
        val tags: List<String>,
        val password: String? = null,
        @ColumnInfo(name = "start_time")
        val startTime: Long = 0L
)

@Entity(tableName = "table_question")
data class Question(
        @PrimaryKey(autoGenerate = false)
        val serial: Int,
        val description: String,
        val type: Int,
        @TypeConverters(ListConverter::class)
        val options: List<String>,
        val marks: Float,
        @TypeConverters(ListConverter::class)
        val answers: List<String> = listOf(),
        @ColumnInfo(name = "image_uri")
        val imageUri: String? = null
)

class QuestionsConverter {

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, Question::class.java)
    private val questionAdapter: JsonAdapter<List<Question>> = moshi.adapter(type)

    @TypeConverter
    fun fromQuestions(questions: List<Question>?): String? {
        return questionAdapter.toJson(questions)
    }

    @TypeConverter
    fun toQuestions(jsonQuestions: String?): List<Question>? {
        return jsonQuestions?.let {  questionAdapter.fromJson(jsonQuestions) }
    }
}

class ListConverter{
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, String::class.java)
    private val listAdapter: JsonAdapter<List<String>> = moshi.adapter(type)

    @TypeConverter
    fun fromList(list: List<String>?) : String? {
        return listAdapter.toJson(list)
    }
    @TypeConverter
    fun toList(jsonList: String?): List<String>? {
        return jsonList?.let { listAdapter.fromJson(jsonList) }
    }
}

@Entity(tableName = "table_response")
data class Response(
        val low: Float,
        val high: Float,
        val message: String,
        val imageUri: String? = null,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L
)

class ResponsesConverter {
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, Response::class.java)
    private val responsesAdapter: JsonAdapter<List<Response>> = moshi.adapter(type)

    @TypeConverter
    fun fromResponses(responses: List<Response>): String? {
        return responsesAdapter.toJson(responses)
    }
    @TypeConverter
    fun toResponses(jsonResponses: String): List<Response>? {
        return responsesAdapter.fromJson(jsonResponses)
    }
}