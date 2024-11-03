package com.example.mybakery.repository

import android.util.Log
import com.example.mybakery.data.model.LoginCredentials
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.data.model.RegisterCredentials
import com.example.mybakery.data.model.RegisterResponse
import com.example.mybakery.data.network.ApiService
import com.example.mybakery.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "AuthRepository"

class AuthRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String): LoginResponse? {
        val credentials = LoginCredentials(email, password)
        Log.d(TAG, "Iniciando sesión para el usuario: $email")

        return try {
            withContext(Dispatchers.IO) {
                apiService.login(credentials)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al intentar iniciar sesión", e)
            null
        }
    }

    suspend fun register(name: String, email: String, password: String, password_confirmation: String): RegisterResponse? {
        val credentials = RegisterCredentials(name, email, password, password_confirmation)
        Log.d(TAG, "Registrando usuario: $email con nombre: $name")

        return try {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "Llamando a la API para registrar")
                val response = apiService.register(credentials)
                Log.d(TAG, "Respuesta de la API: $response")
                response
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al intentar registrar", e)
            null
        }
    }
}
