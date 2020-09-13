package com.example.quizzy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzy.database.*
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSetQuiz = findViewById<MaterialButton>(R.id.button_set_quiz)


        val responses = listOf(
                Response(0F, 25F, "F"), Response(25F, 75F, "A"),
                Response(75F,100F, "A+")
        )
        val questions = listOf(
                Question(1,"Who are political leaders?", MULTIPLE, listOf("Sheikh Mujib", "Api Karim", "Mashrafee"),
                        2F, listOf("Sheikh Mujib", "Mashrafee")),
                Question(2,"What is the value of g?", SINGLE, listOf("9.8", "10", "9.6"),
                        1F, listOf("9.8"))
                )
        val quiz = Quiz(title = "Testing Quiz",
                questions = questions,
                responses = responses,
                tags = listOf("general", "physics"),
                password = "mapnil",
                startTime = System.currentTimeMillis())

        buttonSetQuiz.setOnClickListener {
            val intent = Intent(applicationContext, QuizGameActivity::class.java)
            startActivity(intent)


//            val listConverter = ListConverter()
//            listConverter.fromList(quiz.tags)?.let { json -> Log.i(TAG, listConverter.toList(json).toString()) }
//
//            val responsesConverter = ResponsesConverter()
//            responsesConverter.fromResponses(responses)?.let { json -> Log.i(TAG, responsesConverter.toResponses(json).toString()) }
//
//            val questionsConverter = QuestionsConverter()
//            questionsConverter.fromQuestions(questions)?.let { json -> Log.i(TAG, questionsConverter.toQuestions(json).toString()) }
        }
    }
}