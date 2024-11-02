package com.example.mybakery.data.network

import android.util.Log
import com.example.mybakery.data.model.LoginCredentials
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(@Body credentials: LoginCredentials): LoginResponse


    @GET("users")
    suspend fun getUsers(): List<User>
}