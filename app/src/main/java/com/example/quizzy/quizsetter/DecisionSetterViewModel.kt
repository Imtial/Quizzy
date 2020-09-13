package com.example.quizzy.quizsetter

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository
import com.example.quizzy.database.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DecisionSetterViewModel(private val application: Application): ViewModel() {

    private val repository = QuizRepository(QuizDatabase.getDatabase(application))
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    val totalMarks = repository.getTotalMarks()

    fun insert(vararg responses: Response) {
        coroutineScope.launch {
            repository.insertResponses(*responses)
        }
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}