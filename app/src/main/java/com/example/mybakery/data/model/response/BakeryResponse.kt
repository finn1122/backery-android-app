package com.example.mybakery.data.model.response

data class BakeryResponse(
    val name: String,
    val address: String,
    val opening_hours: String,
    val profile_picture: String?,
    val active: Boolean,
    val id: Int
)
