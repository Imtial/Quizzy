package com.example.quizzy

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.quizzy.quizsetter.QuestionSetterFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class QuizGameActivity: AppCompatActivity() {
    private lateinit var onButtonClickListener: OnButtonClickListener
    private lateinit var topTextView: TextView
    private lateinit var backButton: FloatingActionButton
    private lateinit var completeButton: FloatingActionButton
    private lateinit var nextButton: FloatingActionButton
    private lateinit var spaceOne: Space
    private lateinit var spaceTwo: Space

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_game)

        backButton = findViewById(R.id.button_back)
        spaceOne = findViewById(R.id.space_one)
        completeButton = findViewById(R.id.button_complete)
        spaceTwo = findViewById(R.id.space_two)
        nextButton = findViewById(R.id.button_next)
        topTextView = findViewById(R.id.quiz_game_top)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.game_fragment)
        navigationView.setupWithNavController(navController)

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

    fun showTopTextView () {
        topTextView.visibility = View.VISIBLE
    }

    fun hideTopTextView() {
        topTextView.visibility = View.GONE
    }

    fun showButton(vararg buttonResIds: Int) {
        for (resId in buttonResIds) {
            when(resId) {
                R.id.button_back -> backButton.visibility = View.VISIBLE
                R.id.button_complete -> completeButton.visibility = View.VISIBLE
                R.id.button_next -> nextButton.visibility= View.VISIBLE
            }
        }
        spaceOne.visibility = View.VISIBLE
        spaceTwo.visibility = View.VISIBLE
    }
    fun hideButton(vararg buttonResIds: Int) {
        for (resId in buttonResIds) {
            when(resId) {
                R.id.button_back -> backButton.visibility = View.GONE
                R.id.button_complete -> completeButton.visibility = View.GONE
                R.id.button_next -> {
                    nextButton.visibility = View.GONE
                    spaceTwo.visibility = View.GONE
                }
            }
        }
    }
}

interface OnButtonClickListener {
    fun nextButtonClicked()
    fun completeButtonClicked()
    fun backButtonClicked()
}