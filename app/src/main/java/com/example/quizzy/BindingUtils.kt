package com.example.quizzy

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.quizzy.database.PRIVATE
import com.example.quizzy.database.PUBLIC
import com.example.quizzy.quizsetter.TagListAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

@BindingAdapter("creatorImage")
fun ImageView.setCreatorImage(imageUri: String) {
    Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_player))
            .centerCrop()
            .into(this)
}

@BindingAdapter("startTime")
fun TextView.setStartTime(timeInMillis: Long) {
    if (timeInMillis == 0L) this.visibility = View.GONE
    else {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        this.text = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(calendar.time)
    }
}

@BindingAdapter("duration")
fun TextView.setDuration(minute: Int) {
    if (minute == 0) this.visibility = View.GONE
    else {
        val duration = "$minute min"
        this.text = duration
    }
}

@BindingAdapter("difficulty")
fun TextView.setDifficulty(value: Float) {
    var difficulty = "EASY"
    if (value >= 3 && value < 7) difficulty = "MEDIUM"
    else if (value in 7.0..10.0) difficulty = "HARD"
    this.text = difficulty
}

@BindingAdapter("isPrivate")
fun ImageView.isPrivate(access: String) {
    when(access) {
        PRIVATE -> this.visibility = View.VISIBLE
        PUBLIC -> this.visibility = View.GONE
    }
}

@BindingAdapter("tagList")
fun RecyclerView.setList(items: List<String>) {
    val adapter = TagListAdapter(this.context)
    this.adapter = adapter
    adapter.submitList(items)
}
