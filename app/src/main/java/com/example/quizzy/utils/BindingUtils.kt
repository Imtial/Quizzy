package com.example.quizzy.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.quizzy.R
import com.example.quizzy.domain.PRIVATE
import com.example.quizzy.domain.PUBLIC
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
fun TextView.setTotalQuestions(count: Int?) {
    val text = "You have set $count questions"
    this.text = text
}

@BindingAdapter("creatorImage")
fun ImageView.setCreatorImage(imageUri: String?) {
    Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_player))
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE )
            .skipMemoryCache(true)
            .into(this)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("startTime")
fun TextView.setStartTime(timeInMillis: Long?) {
    if (timeInMillis == null || timeInMillis == 0L) this.visibility = View.GONE
    else {
        this.visibility = View.VISIBLE
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        this.text = "Starts at ${SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(calendar.time)}"
    }
}

@BindingAdapter("duration")
fun TextView.setDuration(minute: Int?) {
    if (minute == 0) this.visibility = View.GONE
    else {
        this.visibility = View.VISIBLE
        val duration = "$minute min"
        this.text = duration
    }
}

@BindingAdapter("difficulty")
fun TextView.setDifficulty(value: Float?) {
    value?.let {
        var difficulty = "EASY"
        if (value >= 3 && value < 7) difficulty = "MEDIUM"
        else if (value in 7.0..10.0) difficulty = "HARD"
        this.text = difficulty
    }
}

@BindingAdapter("isPrivate")
fun ImageView.isPrivate(access: String?) {
    when(access) {
        PRIVATE -> this.visibility = View.VISIBLE
        PUBLIC -> this.visibility = View.GONE
    }
}

@BindingAdapter("questionCount")
fun TextView.setQuestionCount(count: Int?) {
    count?.let {
        var countText = "$count "
        countText += if (count > 1) "questions"
        else "question"
        this.text = countText
    }
}

@BindingAdapter("userCount")
fun TextView.setUserCount(count: Int?) {
    count?.let {
        var countText = "$count "
        countText += if (count > 1) "users"
        else "user"
        this.text = countText
    }
}

@BindingAdapter("tagList")
fun RecyclerView.setList(items: List<String>?) {
    val adapter = TagListAdapter(this.context)
    this.adapter = adapter
    adapter.submitList(items)
}

@BindingAdapter("marks", "total")
fun TextView.setMarks(marks: Float?, total: Float?) {
    if (marks != null && total != null) {
        val text = "You've got $marks out of $total"
        this.text = text
    }
}

@BindingAdapter("correct")
fun TextView.setCorrectCount(correct: Int?) {
    if (correct != null) {
        val text = "Correct: $correct"
        this.text = text
    }
}

@BindingAdapter("wrong")
fun TextView.setWrongCount(wrong: Int?) {
    if (wrong != null) {
        val text = "Wrong: $wrong"
        this.text = text
    }
}

@BindingAdapter("unanswered")
fun TextView.setUnansweredCount(unanswered: Int?) {
    if (unanswered != null) {
        val text = "Unanswered: $unanswered"
        this.text = text
    }
}