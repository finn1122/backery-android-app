package com.example.mybakery.repository

import com.example.mybakery.data.model.User
import com.example.mybakery.data.network.RetrofitClient

class UserRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}