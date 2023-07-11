package com.example.streamingservice.domain.entities

data class SignInRequest(
    val username: String,
    val password: String
)