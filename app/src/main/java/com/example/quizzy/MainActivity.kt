package com.example.quizzy

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.database.QuizRepository
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

//        viewModel.user.observe(this, Observer {
//            Log.i(TAG, "onCreate: $it")
//        })
//
//        viewModel.logInStatus.observe(this, Observer { status ->
//            when(status) {
//                Status.FAILURE -> {
//                    buttonSignUp.visibility = View.VISIBLE
//                    buttonLogIn.visibility = View.GONE
//                }
//                Status.SUCCESS -> {
//                    val intent = Intent(applicationContext, QuizGameActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        })

        buttonAppHome.setOnClickListener {
            val intent = Intent(applicationContext, QuizGameActivity::class.java)
            startActivity(intent)
        }

        buttonLogIn.setOnClickListener {
            var email = ""
            var password = ""
            if (emailInput.text.isNotBlank()) email = emailInput.text.toString().trim()
            if (passwordInput.text.isNotBlank()) password = passwordInput.text.toString().trim()
            viewModel.verifyUser(email, password)
        }
    }

}

enum class Status {
    SUCCESS, FAILURE
}

class MainViewModel(private val application: Application): ViewModel() {

    private val repository = QuizRepository(QuizDatabase.getDatabase(application))

//    private val _logInStatus = MutableLiveData<Status>()
//    val logInStatus: LiveData<Status> get() = _logInStatus

//    val user = repository.currentUser

    fun verifyUser(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.logInUser(email, password)
            }
        }
    }

}
var QUIZ_ITEM_INDEX = 0