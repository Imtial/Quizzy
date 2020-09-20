package com.example.quizzy.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.CachedUser
import com.example.quizzy.domain.UserResponse
import com.example.quizzy.network.NetworkUtil
import com.example.quizzy.network.Status
import com.example.quizzy.task.LogOutTask
import com.example.quizzy.task.LoginTask
import com.example.quizzy.task.SignUpTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepository(private val database: QuizDatabase, private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
    private val networkUtil = NetworkUtil()
    val currentUser = database.userDao.getLiveCachedUser()

    private val _logInStatus = MutableLiveData<Status>()
    val logInStatus : LiveData<Status> get() = _logInStatus

    fun logInUser(email: String, password: String) {
        networkUtil.handleLogin(email, password, object : LoginTask {
            override fun logIn(userResponse: UserResponse?) {
                val user = CachedUser(userResponse?.userInfo?.userId!!, userResponse.token, userResponse.userInfo.name, userResponse.userInfo.email)
                _logInStatus.value = Status.SUCCESS
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        database.userDao.insert(user)
                    }
                }
            }

            override fun onFailure(msg: String?) {
                Log.i("LOGIN", "onFailure: $msg")
                _logInStatus.value = Status.FAILURE
            }
        })
    }

    private val _signUpStatus = MutableLiveData<Status>()
    val signUpStatus : LiveData<Status> get() = _signUpStatus

    fun signUp(name: String, email: String, password: String) {
        networkUtil.handleSignup(name, email, password, object : SignUpTask {
            override fun signUp(userResponse: UserResponse?) {
                val user = CachedUser(userResponse?.userInfo?.userId!!, userResponse.token, userResponse.userInfo.name, userResponse.userInfo.email)
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        database.userDao.clearTable()
                        database.userDao.insert(user)
                    }
                }
            }

            override fun onFailure(msg: String?) {
                Log.i("SIGN-UP", "onFailure: $msg")
                _signUpStatus.value = Status.FAILURE
            }
        })
    }

    private val _logOutStatus = MutableLiveData<Status>()
    val logOutStatus : LiveData<Status> get() = _logOutStatus

    fun logOut() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val token = database.userDao.getUserToken()
                networkUtil.logOut(token, object : LogOutTask {
                    override fun logOut() {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                database.userDao.clearTable()
                                _logOutStatus.value = Status.SUCCESS
                            }
                        }
                    }

                    override fun onFailure(msg: String?) {
                        Log.i("LOGOUT", "onFailure: $msg")
                        _logOutStatus.value = Status.FAILURE
                    }
                })
            }
        }
    }

}