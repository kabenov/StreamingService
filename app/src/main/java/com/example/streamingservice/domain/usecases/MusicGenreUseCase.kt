package com.example.streamingservice.domain.usecases

import com.example.streamingservice.domain.StreamingServiceRepository
import com.example.streamingservice.domain.entities.Genre

class MusicGenreUseCase(private val repository: StreamingServiceRepository) {

    fun getMusicGenres(): List<Genre> {
        return listOf(
            Genre(id = 1, name = "Pop"),
            Genre(id = 2, name = "Rock"),
            Genre(id = 3, name = "Jazz"),
            Genre(id = 4, name = "Rap"),
            Genre(id = 5, name = "Country"),
            Genre(id = 6, name = "Funk"),
            Genre(id = 7, name = "Disco"),
            Genre(id = 8, name = "KPop"),
            Genre(id = 9, name = "Dias"),
            Genre(id = 10, name = "Eeeu")
        )
    }
}