package com.example.quizzy.profile

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.repository.QuizRepository
import com.example.quizzy.repository.UserRepository
import com.example.quizzy.utils.ImageUtil
import okhttp3.MultipartBody
import java.io.File


class ProfileViewModel(private val application: Application) : ViewModel() {

    private val repository = UserRepository(QuizDatabase.getDatabase(application), viewModelScope)

    val user = repository.currentUser

    private val _editEnabled = MutableLiveData<Boolean>()
    val editEnabled : LiveData<Boolean> get() = _editEnabled

    fun enableEdit() { _editEnabled.value = true }
    fun disableEdit() { _editEnabled.value = false}

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    fun init() {
        _imageUri.value = Uri.parse(ImageUtil.generateImageUrl(user.value?.id!!))
    }

    fun setImageUri(imageUri: Uri) {
        _imageUri.value = imageUri
    }

    fun postImage() {
        try {
            val imageFile: File = ImageUtil.convertToFile(application, imageUri.value, "avatar." + ImageUtil.getExtensionFromUri(
                    application, imageUri.value
            ))
            Log.d("imagefile", "total space " + imageFile.totalSpace)
            val body: MultipartBody.Part = ImageUtil.toMultiPartFile("avatar", imageFile)
            repository.postImage(body)
        } catch (e: Exception) {
            e.message?.let { Log.d("Exception =>", it) }
        }
    }

    val updateStatus = repository.updateStatus
}