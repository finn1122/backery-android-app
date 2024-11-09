package com.example.mybakery.repository

import android.util.Log
import com.example.mybakery.data.model.*
import com.example.mybakery.data.model.response.*
import com.example.mybakery.data.network.ApiService
import com.example.mybakery.data.network.RetrofitClient
import com.example.mybakery.utils.PreferencesHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/*private const val TAG = "AuthRepository"

class AuthRepository(
    private val preferencesHelper: PreferencesHelper
) {
    private val apiService: ApiService = RetrofitClient.apiService
    private val gson = Gson()

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val credentials = LoginCredentials(email, password)
            val response = apiService.login(credentials)
            Result.success(response)
        } catch (e: Exception) {
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

    suspend fun verifyBakery(token: String, userId: Int): Response<List<BakeryResponse>> {
        Log.d("AuthRepository", "Calling verifyBakery with token: $token")
        return apiService.verifyBakery(token, userId)
    }

    suspend fun logout() {
        preferencesHelper.clear()
    }
}
*/
