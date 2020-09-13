package com.example.quizzy

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzy.quizsetter.QuestionSetterFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuizGameActivity: AppCompatActivity() {
    private lateinit var onButtonClickListener: OnButtonClickListener
    private lateinit var topTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_game)

        val backButton = findViewById<FloatingActionButton>(R.id.button_back)
        val completeButton = findViewById<FloatingActionButton>(R.id.button_complete)
        val nextButton = findViewById<FloatingActionButton>(R.id.button_next)
        topTextView = findViewById(R.id.quiz_game_top)

        nextButton.setOnClickListener {
            onButtonClickListener.nextButtonClicked()
        }
        completeButton.setOnClickListener {
            onButtonClickListener.completeButtonClicked()
        }
        backButton.setOnClickListener {
            onButtonClickListener.backButtonClicked()
        }
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }

    fun setQuestionNumberOnTopBar (text: String) {
        val spannableString = SpannableString(String.format("Question #%s", text))
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#AA00FF")), 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        topTextView.text = spannableString
    }

    fun setTextOnTopBar (text: String) {
        topTextView.text = text
    }
}

interface OnButtonClickListener {
    fun nextButtonClicked()
    fun completeButtonClicked()
    fun backButtonClicked()
}