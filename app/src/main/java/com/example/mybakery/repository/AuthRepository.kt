package com.example.mybakery.repository

import android.util.Log
import com.example.mybakery.data.model.*
import com.example.mybakery.data.network.ApiService
import com.example.mybakery.data.network.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val TAG = "AuthRepository"

class AuthRepository {
    private val apiService: ApiService = RetrofitClient.apiService
    private val gson = Gson()


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

    suspend fun register(
        name: String,
        email: String,
        password: String,
        password_confirmation: String
    ): Result<RegisterResponse> {
        val credentials = RegisterCredentials(name, email, password, password_confirmation)
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.register(credentials)
                Result.success(response)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
            Result.failure(Exception(errorResponse.errors.flatMap { it.value }.joinToString("\n")))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al intentar registrar", e)
            Result.failure(e)
        }
    }

    suspend fun resendVerificationEmail(): VerificationEmailResponse? {
        return try {
            withContext(Dispatchers.IO) {
                apiService.resendVerificationEmail()
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al reenviar correo de verificación", e)
            null
        }
    }
}
