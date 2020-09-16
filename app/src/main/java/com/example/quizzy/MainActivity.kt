package com.example.quizzy

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository
import com.example.quizzy.domain.UserInfo
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val emailInput = findViewById<EditText>(R.id.emailEditText)
        val passwordInput = findViewById<EditText>(R.id.passEditText)
        val buttonLogIn = findViewById<MaterialButton>(R.id.loginButton)
        val buttonSignUp = findViewById<MaterialButton>(R.id.signupButton)
        val buttonAppHome = findViewById<MaterialButton>(R.id.button_app_home)

        val viewModel = ViewModelProvider(this, ViewModelFactory(application)).get(MainViewModel::class.java)

        viewModel.user.observe(this, Observer {
            Log.i(TAG, "onCreate: $it")
        })

        viewModel.logInStatus.observe(this, Observer { status ->
            when(status) {
                Status.FAILURE -> {
                    buttonSignUp.visibility = View.VISIBLE
                    buttonLogIn.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    val intent = Intent(applicationContext, QuizGameActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })

        buttonAppHome.setOnClickListener {
            val intent = Intent(applicationContext, QuizGameActivity::class.java)
            startActivity(intent)
        }

        buttonLogIn.setOnClickListener {
            if (emailInput.text.isNotBlank()) viewModel.userInfo.email = emailInput.text.toString().trim()
            if (passwordInput.text.isNotBlank()) viewModel.userInfo.password = passwordInput.text.toString().trim()
            viewModel.verifyUser()
        }
    }

}

enum class Status {
    SUCCESS, FAILURE
}

class MainViewModel(private val application: Application): ViewModel() {

    private val repository = QuizRepository(QuizDatabase.getDatabase(application))

    private val _logInStatus = MutableLiveData<Status>()
    val logInStatus: LiveData<Status> get() = _logInStatus

    val userInfo = UserInfo()

    val user = repository.currentUser

    fun verifyUser() {
        if (userInfo.email == user.value?.email
                && userInfo.password == user.value?.password) {
            _logInStatus.value = Status.SUCCESS
        } else {
            _logInStatus.value = Status.FAILURE
        }
    }

    fun saveUser() {
        userInfo.createdAt = Date(System.currentTimeMillis())
        userInfo.updatedAt = Date(System.currentTimeMillis())
        userInfo._id = "tXkn7ys5G"
        userInfo.name = "Shahrar"
        userInfo.token = "some-very-very-weird-token"
//        if (userInfo.name == null || userInfo.password == null) {
//            _logInStatus.value = Status.FAILURE
//            return
//        }
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.insertUser(userInfo)
                withContext(Dispatchers.Main) {
                    _logInStatus.value = Status.SUCCESS
                }
            }
        }
    }
}
var QUIZ_ITEM_INDEX = 0