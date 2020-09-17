package com.example.quizzy.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository

class ProfileViewModel(private val application: Application) : ViewModel() {

    private val repository = QuizRepository(QuizDatabase.getDatabase(application))

    val user = repository.currentUser

    private val _editEnabled = MutableLiveData<Boolean>()
    val editEnabled : LiveData<Boolean> get() = _editEnabled

    fun enableEdit() { _editEnabled.value = true }
    fun disableEdit() { _editEnabled.value = false}

}