package com.example.mybakery.data.network

import android.util.Log
import com.example.mybakery.data.model.LoginCredentials
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.data.model.RegisterResponse
import com.example.mybakery.data.model.User
import com.example.mybakery.data.model.RegisterCredentials
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse

    @POST("register")
    suspend fun register(@Body credentials: RegisterCredentials): RegisterResponse

    @GET("users")
    suspend fun getUsers(): List<User>
}
