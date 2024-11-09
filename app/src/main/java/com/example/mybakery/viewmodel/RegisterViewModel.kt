package com.example.mybakery.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybakery.data.network.RetrofitClient
import androidx.lifecycle.viewModelScope
import com.example.mybakery.data.model.RegisterCredentials
import com.example.mybakery.data.model.response.EmailResendResponse
import com.example.mybakery.data.model.response.RegisterResponse
import com.example.mybakery.data.model.response.VerificationEmailCredentials
import com.example.mybakery.data.model.response.VerificationEmailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {

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

    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess : LiveData<Boolean> = _isRegisterSuccess

    private val _isResendEmailSuccess = MutableLiveData<Boolean>()
    val isResendEmailSuccess : LiveData<Boolean> = _isResendEmailSuccess

    private val _canSendEmail = MutableLiveData<Boolean>()
    val canSendEmail : LiveData<Boolean> = _canSendEmail

    private var resendEmailAttempts = 0

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    private val _resendEmailResult = MutableLiveData<Result<String>>()
    val resendEmailResult: LiveData<Result<String>> = _resendEmailResult

    private val _timerValue = MutableLiveData<Int>()
    val timerValue: LiveData<Int> = _timerValue


    private fun isValidName(name: String): Boolean = name.length >= 3

    private fun isValidPassword(password: String): Boolean = password.length >= 6

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun passwordsMatch(password: String, passwordConfirmation: String): Boolean = password == passwordConfirmation

    private fun startEmailResendTimer() {
        viewModelScope.launch {
            _canSendEmail.value = false
            for (time in 60 downTo 1) {
                _timerValue.value = time
                delay(1000) // Esperar 1 segundo
            }
            _canSendEmail.value = true
        }
    }

    fun resendVerificationEmail() {
        if (resendEmailAttempts < 3) { // Verifica si el número de intentos es menor a 3
            viewModelScope.launch {
                val emailCredentials = VerificationEmailCredentials(
                    email = _email.value ?: return@launch // Retorna temprano si el email es nulo
                )
                try {
                    _isLoading.value = true
                    val response: VerificationEmailResponse = withContext(Dispatchers.IO) {
                        apiService.resendVerificationEmail(emailCredentials)
                    }
                    val successMessage = response.message
                    _resendEmailResult.value = Result.success(successMessage)
                    _isResendEmailSuccess.value = true
                    resendEmailAttempts++ // Incrementa el contador de intentos si es exitoso
                    startEmailResendTimer() // Inicia el temporizador

                } catch (e: Exception) {
                    _resendEmailResult.value = Result.failure(e)
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            _resendEmailResult.value = Result.failure(Exception("Maximum resend attempts reached"))
        }
    }

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

    fun onRegisterSelected(){
        _isLoading.value = true
        viewModelScope.launch {
            val registerCredentials = RegisterCredentials(
                name = _name.value ?: "",
                email = _email.value ?: "",
                password = _password.value ?: "",
                password_confirmation = _passwordConfirmation.value ?: ""
            )

            try {
                val response: RegisterResponse = withContext(Dispatchers.IO) { apiService.register(registerCredentials) }
                val successMessage = response.token // Asegúrate de utilizar `token` como mensaje de éxito
                _registerResult.value = Result.success(successMessage)
                _isRegisterSuccess.value = true
                startEmailResendTimer()

            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
                _isRegisterSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}


