package com.example.mybakery.data.model

data class RegisterCredentials(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String // Campo adicional para confirmación de contraseña
)
