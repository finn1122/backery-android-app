package com.example.mybakery.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val roles: List<String>
)
