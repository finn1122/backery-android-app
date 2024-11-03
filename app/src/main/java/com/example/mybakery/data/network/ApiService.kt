package com.example.mybakery.data.network

import android.util.Log
import com.example.mybakery.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse

    @POST("register")
    suspend fun register(@Body credentials: RegisterCredentials): RegisterResponse

    @POST("email/verification-notification")
    suspend fun resendVerificationEmail(@Body body: Map<String, String>): VerificationEmailResponse

    @GET("user/bakery")
    suspend fun verifyBakery(@Header("Authorization") token: String): Response<List<BakeryResponse>>

}
