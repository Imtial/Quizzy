package com.example.quizzy.homepage

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val adapter = QuizListAdapter()
        val quizListView = findViewById<RecyclerView>(R.id.quiz_list)
        quizListView.adapter = adapter

        val viewModel = ViewModelProvider(this, ViewModelFactory(application)).get(HomeViewModel::class.java)

        viewModel.liveQuizItemList.observe(this, {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
    }
}