package com.dicoding.picodiploma.loginwithanimation.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.GetDetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.UploadResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> get() = _uploadStatus

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadStory(token: String, imageFile: MultipartBody.Part, desc: RequestBody) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImage(token, imageFile, desc)
                if (response.error == false) {
                    _uploadStatus.postValue(true)
                } else {
                    _uploadStatus.postValue(false)
                }
            } catch (e: Exception) {
                _uploadStatus.postValue(false)
            }
        }
    }
}
