package com.example.mybakery.data.model.response
import com.example.mybakery.data.model.user.User

data class LoginResponse(
    val token: String, // Suponiendo que la respuesta contiene un token
    val user: User // Suponiendo que la respuesta también contiene la información del usuario
)
