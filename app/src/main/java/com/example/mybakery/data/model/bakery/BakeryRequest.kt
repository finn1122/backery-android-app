package com.example.mybakery.data.model.bakery

data class BakeryRequest(
    val name: String,
    val address: String,
    val openingHours: String,
    val profilePicture: String?, // base64-encoded image or null
    val active: Boolean
)
