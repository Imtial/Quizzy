package com.example.quizzy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzy.database.*
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSetQuiz = findViewById<MaterialButton>(R.id.button_set_quiz)

        CoroutineScope(Dispatchers.IO).launch {
            val quizList = QuizDatabase.getDatabase(applicationContext).quizDao.getQuizList()
            for (quiz in quizList)
                Log.i("QUIZ_INSERT", "PUBLISH: $quiz")
        }
        buttonSetQuiz.setOnClickListener {
            val intent = Intent(applicationContext, QuizGameActivity::class.java)
            startActivity(intent)
        }
    }
}