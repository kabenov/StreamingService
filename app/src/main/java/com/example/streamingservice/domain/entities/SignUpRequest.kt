package com.example.streamingservice.domain.entities

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
)
