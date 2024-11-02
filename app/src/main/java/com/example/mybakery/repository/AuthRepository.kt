package com.example.mybakery.repository

import android.util.Log
import com.example.mybakery.data.model.LoginCredentials
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.data.network.ApiService
import com.example.mybakery.data.network.RetrofitClient

class AuthRepository {

    // Cambia el nombre de tu clase si es necesario
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String): LoginResponse {
        val credentials = LoginCredentials(email, password)

        // Log de la URL y los datos que se están enviando
        val loginUrl = "${RetrofitClient.BASE_URL}login"
        Log.d("AuthRepository", "Iniciando sesión para el usuario: $email")
        Log.d("AuthRepository", "Intentando iniciar sesión con: $credentials")
        Log.d("AuthRepository", "URL de la solicitud: $loginUrl")

        return apiService.login(credentials) // Realiza la solicitud POST aquí
    }
}