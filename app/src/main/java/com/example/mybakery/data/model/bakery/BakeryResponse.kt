package com.example.mybakery.data.model.bakery

data class BakeryResponse(
    val name: String,
    val address: String,
    val openingHours: String,
    val profilePicture: String?,
    val active: Boolean,
    val id: Int
)
