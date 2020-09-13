package com.example.quizzy

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("totalMarks")
fun TextView.setTotalMarks(marks: Float) {
    val text = "Total Marks $marks"
    this.text = text
}

@BindingAdapter("totalQuestions")
fun TextView.setTotalMarks(count: Int) {
    val text = "You have set $count questions"
    this.text = text
}
