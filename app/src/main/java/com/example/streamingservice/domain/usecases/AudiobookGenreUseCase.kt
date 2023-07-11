package com.example.streamingservice.domain.usecases

import com.example.streamingservice.domain.StreamingServiceRepository
import com.example.streamingservice.domain.entities.Genre

class AudiobookGenreUseCase(private val repository: StreamingServiceRepository) {

    fun getAudiobookGenres(): List<Genre> {
        return listOf(
            Genre(id = 1, name = "Көркем әдебиет"),
            Genre(id = 2, name = "Балалар әдебиеті"),
            Genre(id = 3, name = "Психологиялық әдебиет"),
            Genre(id = 4, name = "Іскерлік кітаптар"),
            Genre(id = 5, name = "Көркем әдебиет"),
            Genre(id = 6, name = "Балалар әдебиеті"),
            Genre(id = 7, name = "Психологиялық әдебиет"),
            Genre(id = 8, name = "Іскерлік кітаптар")
        )
    }
}