package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.RegisterResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository): ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: (RegisterResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, email, password)
                onSuccess(response)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}