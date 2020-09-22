package com.example.quizzy

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.network.Status
import com.example.quizzy.repository.UserRepository
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"

    private var isSignUpScreen = false

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var buttonLogIn: MaterialButton
    private lateinit var textOr: TextView
    private lateinit var buttonSignUp: MaterialButton
    private lateinit var buttonCreateAccount: MaterialButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameInput = findViewById(R.id.nameEditText)
        emailInput = findViewById(R.id.emailEditText)
        passwordInput = findViewById(R.id.passEditText)
        buttonLogIn = findViewById(R.id.loginButton)
        textOr = findViewById(R.id.textview_or)
        buttonSignUp = findViewById(R.id.signupButton)
        buttonCreateAccount = findViewById(R.id.button_create_account)
        progressBar = findViewById(R.id.loading_progress_bar)

        val viewModel = ViewModelProvider(this, ViewModelFactory(application)).get(MainViewModel::class.java)

        viewModel.user.observe(this, Observer {user ->
            Log.i(TAG, "onCreate: $user")
            user?.let {
                val intent = Intent(applicationContext, QuizGameActivity::class.java)
                startActivity(intent)
                progressBar.visibility = View.GONE
                finish()
            }
        })

        // Log In Failed
        viewModel.logInStatus.observe(this, Observer { status ->
            if (status == Status.FAILURE) {
                progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "Log in failed! Please provide correct email & password", Toast.LENGTH_LONG).show()
            }
        })

        // Sign Up Failed
        viewModel.signUpStatus.observe(this, Observer { status ->
            if (status == Status.FAILURE) {
                progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "Sign up failed! Please provide valid email & password", Toast.LENGTH_LONG).show()
            }
        })

        buttonCreateAccount.setOnClickListener {
            setUpSignUpScreen()
        }

        buttonLogIn.setOnClickListener {
            if (emailInput.text.isNotBlank() &&
                    passwordInput.text.isNotBlank()) {
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                if (password.length < 6) passwordInput.error = "Length of password must be at least 6"
                else {
                    progressBar.visibility = View.VISIBLE
                    viewModel.verifyUser(email, password)
                }
            }
            else {
                Toast.makeText(applicationContext, "Fill up all fields", Toast.LENGTH_LONG).show()
            }
        }

        buttonSignUp.setOnClickListener {
            if (nameInput.text.isNotBlank() &&
                    emailInput.text.isNotBlank() &&
                    passwordInput.text.isNotBlank()) {
                val name = nameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                if (password.length < 6) passwordInput.error = "Length of password must be at least 6"
                else {
                    progressBar.visibility = View.VISIBLE
                    viewModel.signUp(name, email, password)
                }
            }
            else {
                Toast.makeText(applicationContext, "Fill up all fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpLogInScreen() {
        isSignUpScreen = false
        nameInput.visibility = View.GONE
        buttonSignUp.visibility = View.GONE
        buttonLogIn.visibility = View.VISIBLE
        buttonCreateAccount.visibility = View.VISIBLE
        textOr.visibility = View.VISIBLE
    }

    private fun setUpSignUpScreen() {
        isSignUpScreen = true
        nameInput.visibility = View.VISIBLE
        buttonSignUp.visibility = View.VISIBLE
        buttonLogIn.visibility = View.GONE
        buttonCreateAccount.visibility = View.GONE
        textOr.visibility = View.GONE
    }

    override fun onBackPressed() {
        when(isSignUpScreen) {
            true -> setUpLogInScreen()
            false -> super.onBackPressed()
        }
    }

}


class MainViewModel(private val application: Application): ViewModel() {

    private val repository = UserRepository(QuizDatabase.getDatabase(application), viewModelScope)

    val logInStatus = repository.logInStatus
    val signUpStatus = repository.signUpStatus

    val user = repository.currentUser

    fun verifyUser(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.logInUser(email, password)
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        repository.signUp(name, email, password)
    }

}
var QUIZ_ITEM_INDEX = 0