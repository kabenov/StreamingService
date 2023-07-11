package com.example.streamingservice.domain.entities

data class SignInUpResponse(
    val user: User?,
    val message: String,
    val status: Int
)