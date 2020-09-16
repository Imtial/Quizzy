package com.example.quizzy

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.homepage.HomeViewModel
import com.example.quizzy.quizgame.GameViewModel
import com.example.quizzy.quizsetter.DecisionSetterViewModel
import com.example.quizzy.quizsetter.PublishQuizViewModel
import com.example.quizzy.quizsetter.QuestionSetterViewModel


class ViewModelFactory (private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionSetterViewModel::class.java)) {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        } else if (modelClass.isAssignableFrom(DecisionSetterViewModel::class.java)) {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        } else if (modelClass.isAssignableFrom(PublishQuizViewModel::class.java)) {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

class NavGraphViewModelFactory(private val application: Application, private val arg: String)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return modelClass.getConstructor(Application::class.java, String::class.java).newInstance(application, arg)
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}