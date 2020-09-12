package com.example.quizzy

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.quizsetter.QuestionSetterViewModel


class ViewModelFactory (private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionSetterViewModel::class.java)) {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}