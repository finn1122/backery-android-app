package com.example.mybakery.data.network

import com.example.mybakery.data.model.*
import com.example.mybakery.data.model.bakery.BakeryRequest
import com.example.mybakery.data.model.bakery.BakeryResponse
import com.example.mybakery.data.model.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse

    @POST("register")
    suspend fun register(@Body credentials: RegisterCredentials): RegisterResponse

    @POST("email/verification-notification")
    suspend fun resendVerificationEmail(@Body body: VerificationEmailCredentials): VerificationEmailResponse

    @GET("user/{userId}/bakery")
    suspend fun verifyBakery(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<List<BakeryResponse>> // Cambiar el tipo de respuesta a List<BakeryResponse>

    // Método para crear una panadería
    @POST("user/{userId}/bakery")
    suspend fun createBakery(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Body request: BakeryRequest
    ): Response<BakeryResponse>

    // Método para actualizar una panadería
    @PUT("user/{userId}/bakery/{bakeryId}")
    suspend fun updateBakery(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Path("bakeryId") bakeryId: Int,
        @Body request: BakeryRequest
    ): Response<BakeryResponse>
}
