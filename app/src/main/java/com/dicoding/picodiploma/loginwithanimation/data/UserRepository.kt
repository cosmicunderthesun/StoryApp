package com.dicoding.picodiploma.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSourceFactory
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.GetDetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.GetStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.UploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.StoryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return withContext(Dispatchers.IO) {
            apiService.register(name, email, password)
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            apiService.login(email, password)
        }
    }

    suspend fun getStory(token: String): GetStoryResponse {
        return withContext(Dispatchers.IO) {
            apiService.getStories("Bearer $token")
        }
    }

    suspend fun getListStory(token: String): List<ListStoryItem> {
        return withContext(Dispatchers.IO) {
            apiService.getStories("Bearer $token").listStory
        }
    }

    suspend fun getDetailStory(token: String, id: String): GetDetailResponse {
        return withContext(Dispatchers.IO) {
            apiService.getDetailStory("Bearer $token", id)
        }
    }

    suspend fun uploadImage(
        token: String,
        imageFile: MultipartBody.Part,
        description: RequestBody
    ): UploadResponse {
        return withContext(Dispatchers.IO) {
            apiService.uploadImage("Bearer $token", imageFile, description)
        }
    }

    suspend fun getStoryWithLocation(
        token: String
    ): GetStoryResponse {
        return withContext(Dispatchers.IO) {
            apiService.getStoriesWithLocation("Bearer $token")
        }
    }

    fun getPagingStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false // Optional: Set to true if you want to use placeholders
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, "Bearer $token")
            }
        ).liveData
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}