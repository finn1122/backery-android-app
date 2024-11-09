package com.example.mybakery.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mybakery.data.network.RetrofitClient

class RegisterViewModel {

    private val apiService = RetrofitClient.apiService

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _passwordConfirmation = MutableLiveData<String>()
    val passwordConfirmation : LiveData<String> = _passwordConfirmation

    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable : LiveData<Boolean> = _registerEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _canSendEmail = MutableLiveData<Boolean>()
    val canSendEmail : LiveData<Boolean> = _canSendEmail

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    private fun isValidName(name: String): Boolean = name.length >= 3

    private fun isValidPassword(password: String): Boolean = password.length >= 6

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun passwordsMatch(password: String, passwordConfirmation: String): Boolean = password == passwordConfirmation


    fun onRegisterChanged(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ){
        _name.value = name
        _email.value = email
        _password.value = password
        _passwordConfirmation.value = passwordConfirmation
        _registerEnable.value = isValidName(name) && isValidEmail(email) && isValidPassword(password) && passwordsMatch(password, passwordConfirmation)
    }
}


