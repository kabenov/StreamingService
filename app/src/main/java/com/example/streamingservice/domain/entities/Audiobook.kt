package com.example.streamingservice.domain.entities

data class Audiobook(
    val id: String,
    val name: String,
    val author: String,
    val dictor: String,
    val description: String,
    val posterUrl: String
)