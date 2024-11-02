package com.example.mybakery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var loginResult: LoginResponse? = null
    var errorMessage: String? = null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                loginResult = authRepository.login(email, password)
                // Manejar el resultado de login (por ejemplo, guardar el token)
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
}