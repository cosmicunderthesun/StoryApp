package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.GetDetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.GetStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyResponse = MutableLiveData<List<ListStoryItem>>() // Ubah ke List
    val storyResponse: LiveData<List<ListStoryItem>> get() = _storyResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStory(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStory(token)
                _storyResponse.postValue(response.listStory)
                Log.d("MainViewModel", "Story fetched: ${response.listStory.size} items")
            } catch (e: Exception) {
                _storyResponse.postValue(emptyList())
            }
        }
    }
}
