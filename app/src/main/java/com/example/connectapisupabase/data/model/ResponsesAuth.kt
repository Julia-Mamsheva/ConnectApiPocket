package com.example.connectapisupabase.data.model

data class ResponsesAuth(
    val token: String,
    val record: UserResponse,
)