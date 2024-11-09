package com.example.mybakery.data.network

import android.util.Log
import com.example.mybakery.data.model.*
import com.example.mybakery.data.model.response.BakeryResponse
import com.example.mybakery.data.model.response.LoginResponse
import com.example.mybakery.data.model.response.RegisterResponse
import com.example.mybakery.data.model.response.VerificationEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse

    @POST("register")
    suspend fun register(@Body credentials: RegisterCredentials): RegisterResponse

    @POST("email/verification-notification")
    suspend fun resendVerificationEmail(@Body body: Map<String, String>): VerificationEmailResponse

    @GET("user/{userId}/bakery")
    suspend fun verifyBakery(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<List<BakeryResponse>>
}
