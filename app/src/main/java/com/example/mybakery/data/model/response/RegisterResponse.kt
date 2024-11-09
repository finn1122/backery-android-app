package com.example.mybakery.data.model.response
import com.example.mybakery.data.model.user.User

data class RegisterResponse(
    val user: User,
    val token: String
)
