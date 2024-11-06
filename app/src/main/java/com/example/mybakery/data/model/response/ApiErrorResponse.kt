package com.example.mybakery.data.model

data class ApiErrorResponse(
    val errors: Map<String, List<String>>
)
