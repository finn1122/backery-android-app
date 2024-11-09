package com.example.mybakery.data.model.response

data class ApiErrorResponse(
    val errors: Map<String, List<String>>
)
