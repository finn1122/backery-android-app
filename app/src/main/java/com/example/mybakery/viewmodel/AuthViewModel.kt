package com.example.mybakery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var loginResult: LoginResponse? by mutableStateOf(null)
    var errorMessage: String? by mutableStateOf(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess { response ->
                loginResult = response
                // Manejar el resultado de login (por ejemplo, guardar el token)
            }.onFailure { throwable ->
                errorMessage = throwable.message
            }
        }
    }
}
