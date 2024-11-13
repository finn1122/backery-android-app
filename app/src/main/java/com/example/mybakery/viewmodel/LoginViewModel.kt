package com.example.mybakery.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybakery.data.model.LoginCredentials
import com.example.mybakery.data.model.bakery.BakeryResponse
import com.example.mybakery.data.network.RetrofitClient
import com.example.mybakery.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable : LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    private val _isLoginSuccess = MutableLiveData<Boolean>()
    val isLoginSuccess : LiveData<Boolean> = _isLoginSuccess

    private val _bakeryResult = MutableLiveData<Result<List<BakeryResponse>?>>()
    val bakeryResult: LiveData<Result<List<BakeryResponse>?>> = _bakeryResult

    fun onLoginChanged(email: String, password: String){
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length > 6

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun resetLoginSuccess() {
        _isLoginSuccess.value = false
    }

    fun onLoginSelected(context: Context){
        _isLoading.value = true
        viewModelScope.launch {
            val credentials = LoginCredentials(
                email = _email.value ?: "",
                password = _password.value ?: ""
            )

            try {
                _isLoading.value = true
                val response = withContext(Dispatchers.IO) {
                    apiService.login(credentials)
                }
                val successMessage = response.token
                _loginResult.value = Result.success(successMessage)

                // Guardar el token y los datos del usuario en SharedPreferences
                val preferencesHelper = PreferencesHelper(context)
                preferencesHelper.saveToken(response.token)
                preferencesHelper.saveUserName(response.user.name)
                preferencesHelper.saveUserEmail(response.user.email)
                // Suponiendo que el usuario puede tener múltiples roles, guarda el primero en este ejemplo
                if (response.user.roles.isNotEmpty()) {
                    preferencesHelper.saveUserRole(response.user.roles[0])
                }
                Log.d("LoginViewModel", "token: ${response.token}")
                verifyBakery(response.token, response.user.id)
                _isLoginSuccess.value = true

            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun verifyBakery(token: String, userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Asegurar que el token tiene el prefijo correcto
                val authHeader = "Bearer $token"
                val response = withContext(Dispatchers.IO) {
                    apiService.verifyBakery(authHeader, userId) // Enviando el token correctamente como Bearer token
                }
                if (response.isSuccessful) {
                    val bakeryList = response.body()
                    Log.d("LoginViewModel", "Bakery ID: ${bakeryList}")

                    _bakeryResult.value = Result.success(bakeryList)
                    bakeryList?.forEach { bakery ->
                        Log.d("LoginViewModel", "Bakery ID: ${bakery.id}")
                        Log.d("LoginViewModel", "Bakery Name: ${bakery.name}")
                    }
                } else {
                    val error = "Error fetching bakeries, status code: ${response.code()}"
                    _bakeryResult.value = Result.failure(Exception(error))
                    Log.d("LoginViewModel", "Error response body: ${response.errorBody()?.string().orEmpty()}")
                }
            } catch (e: Exception) {
                _bakeryResult.value = Result.failure(e)
                Log.d("LoginViewModel", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
