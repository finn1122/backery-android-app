package com.example.mybakery.repository

import android.util.Log
import com.example.mybakery.data.model.*
import com.example.mybakery.data.network.ApiService
import com.example.mybakery.data.network.RetrofitClient
import com.example.mybakery.utils.PreferencesHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val TAG = "AuthRepository"

class AuthRepository(
    private val preferencesHelper: PreferencesHelper
) {
    private val apiService: ApiService = RetrofitClient.apiService
    private val gson = Gson()

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        val credentials = LoginCredentials(email, password)
        Log.d(TAG, "Iniciando sesión para el usuario: $email")

        return try {
            val loginResponse = withContext(Dispatchers.IO) {
                apiService.login(credentials)
            }

            // Almacenar el token y los datos del usuario en SharedPreferences
            preferencesHelper.saveToken(loginResponse.token)
            preferencesHelper.saveUserName(loginResponse.user.name)
            preferencesHelper.saveUserEmail(loginResponse.user.email)
            if (loginResponse.user.roles.isNotEmpty()) {
                preferencesHelper.saveUserRole(loginResponse.user.roles[0])
            }

            Result.success(loginResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Error al intentar iniciar sesión", e)
            Result.failure(e)
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

    suspend fun resendVerificationEmail(email: String): Result<VerificationEmailResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Asegúrate de enviar el email en el cuerpo de la solicitud, si es necesario
                val response = apiService.resendVerificationEmail(mapOf("email" to email))
                Result.success(response)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun verifyBakery(): Result<BakeryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesHelper.getToken()
                // Suponiendo que uses Retrofit para hacer la llamada a la API
                val response = apiService.verifyBakery("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error en la verificación de la panadería"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun logout() {
        preferencesHelper.clear() // Limpiar datos al cerrar sesión
    }
}
