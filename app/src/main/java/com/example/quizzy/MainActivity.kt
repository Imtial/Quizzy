package com.example.quizzy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzy.database.*
import com.example.quizzy.homepage.HomeActivity
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
        val buttonInsertQuiz = findViewById<MaterialButton>(R.id.button_insert_quiz)
        val buttonClearDB = findViewById<MaterialButton>(R.id.button_clear_db)

        buttonSetQuiz.setOnClickListener {
            val intent = Intent(applicationContext, QuizGameActivity::class.java)
            startActivity(intent)
        }

        buttonInsertQuiz.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch{
//                if (QUIZ_ITEM_INDEX < QUIZZES.size) {
//                    QuizRepository(QuizDatabase.getDatabase(applicationContext))
//                            .insertQuizItem(QUIZZES[QUIZ_ITEM_INDEX++])
//                }
//            }
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        buttonClearDB.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                QuizRepository(QuizDatabase.getDatabase(applicationContext)).clearQuizItemTable()
            }
        }
    }
}
var QUIZ_ITEM_INDEX = 0