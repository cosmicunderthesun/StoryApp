package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository): ViewModel() {
    private val _storyResponse = MutableLiveData<List<ListStoryItem>>()
    val storyResponse: LiveData<List<ListStoryItem>> get() = _storyResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStoryWithLocation(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStoryWithLocation(token)
                _storyResponse.postValue(response.listStory)
            } catch (e: Exception) {
                _storyResponse.postValue(emptyList())
            }
        }
    }
}