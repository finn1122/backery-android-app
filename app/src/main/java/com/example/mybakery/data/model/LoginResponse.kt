package com.example.mybakery.data.model

data class LoginResponse(
    val token: String, // Suponiendo que la respuesta contiene un token
    val user: User // Suponiendo que la respuesta también contiene la información del usuario
)