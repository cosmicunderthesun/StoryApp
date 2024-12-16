package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.GetDetailResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository): ViewModel() {
    private val _detailResponse = MutableLiveData<GetDetailResponse>()
    val detailResponse: LiveData<GetDetailResponse> get() = _detailResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getDetailStory(token: String, id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailStory(token, id)
                _detailResponse.postValue(response)
            } catch (e: Exception) {
                _detailResponse.postValue(GetDetailResponse(error = true, message = e.message))
            }
        }
    }
}