package com.example.quizzy.domain

import androidx.room.*
import com.example.quizzy.keyGen
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.util.*

const val SINGLE = "SINGLE"
const val MCQ = "MCQ"
const val TEXT = "TEXT"
const val NOPASSWORD = "NO_PASSWORD"
const val PUBLIC = "PUBLIC"
const val PRIVATE = "PRIVATE"

@JsonClass(generateAdapter = true)
@Entity(tableName = "table_quiz")
data class CachedQuiz(
        @PrimaryKey
        val id: String = keyGen(),
        var title: String = "untitled",
        @TypeConverters(QuestionResponsesConverter::class)
        var questionResponses: List<QuestionResponse> = listOf(),
        @TypeConverters(ResponsesConverter::class)
        var respons: List<Response> = listOf(),
        @TypeConverters(ListConverter::class)
        var tags: List<String> = listOf(),
        var password: String = NOPASSWORD,
        @ColumnInfo(name = "start_time")
        var startTime: Long = 0L,
        var duration: Int = 0
)

@Entity(tableName = "table_question")
open class Question() {

    @PrimaryKey var description: String = ""
        var type: String? = PUBLIC
        @TypeConverters(ListConverter::class)
        var options: List<String>? = listOf()
        var marks: Float = 0F
        @TypeConverters(ListConverter::class)
        var answers: List<String>? = listOf()
//        @ColumnInfo(name = "image_uri")
//        val imageUri: String? = null


    constructor(description: String, type: String?, options: List<String>?, marks: Float, answers: List<String>?) : this() {
        this.description = description
        this.type = type
        this.options = options
        this.marks = marks
        this.answers = answers
    }

    override fun toString(): String {
        return "Question(description=$description, type=$type, options=$options, marks=$marks, answers=$answers)"
    }


}

class QuestionResponsesConverter {

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, QuestionResponse::class.java)
    private val questionResponseAdapter: JsonAdapter<List<QuestionResponse>> = moshi.adapter(type)

    @TypeConverter
    fun fromQuestions(questionResponses: List<QuestionResponse>?): String? {
        return questionResponseAdapter.toJson(questionResponses)
    }

    @TypeConverter
    fun toQuestions(jsonQuestionResponses: String?): List<QuestionResponse>? {
        return jsonQuestionResponses?.let {  questionResponseAdapter.fromJson(jsonQuestionResponses) }
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

@JsonClass(generateAdapter = true)
@Entity(tableName = "table_response", primaryKeys = ["low", "high"])
data class Response(
        val low: Float,
        val high: Float,
        val message: String,
        @SerializedName("_id")
        val id: String? = null
)

class ResponsesConverter {
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, Response::class.java)
    private val responsesAdapter: JsonAdapter<List<Response>> = moshi.adapter(type)

    @TypeConverter
    fun fromResponses(respons: List<Response>): String? {
        return responsesAdapter.toJson(respons)
    }
    @TypeConverter
    fun toResponses(jsonResponses: String): List<Response>? {
        return responsesAdapter.fromJson(jsonResponses)
    }
}

class DateConverter {
    @TypeConverter
    fun fromDate(date: Date) : Long? = date.time

    @TypeConverter
    fun toDate(timeInMillis: Long) : Date? = Date(timeInMillis)
}

@Entity(tableName = "table_quiz_item")
data class QuizItem (
        @PrimaryKey
        val id: String,
        val title: String,
        @ColumnInfo(name = "question_count")
        val questionCount: Int,
        val marks: Float,
        @ColumnInfo(name = "start_time")
        val startTime: Long,
        val duration: Int,
        @ColumnInfo(name = "user_count")
        val userCount: Int,
        val tags: List<String>,
        val difficulty: Float,
        val rating: Float,
        val access: String,
        @ColumnInfo(name = "creator_name")
        val creatorName: String,
        @ColumnInfo(name = "creator_image_uri")
        val creatorImageUri: String = ""
)