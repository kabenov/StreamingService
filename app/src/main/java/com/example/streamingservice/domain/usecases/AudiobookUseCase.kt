package com.example.streamingservice.domain.usecases

import androidx.lifecycle.LiveData
import com.example.streamingservice.domain.StreamingServiceRepository
import com.example.streamingservice.domain.entities.Audiobook
import com.example.streamingservice.domain.entities.Genre
import com.example.streamingservice.domain.entities.Music

class AudiobookUseCase(private val repository: StreamingServiceRepository) {

    fun getRecommendedAudiobook(): List<Audiobook> {
        val genre = Genre(
            id = 1,
            name = "Көркем әдебиет"
        )

        return listOf(
            Audiobook(
                id = "0",
                name = "Қан мен тер",
                author = "Әбдіжәміл Нұрпейсов",
                dictor = "Nurka",
                description = "Audiobook",
                posterUrl = "https://lmusic.kz/images/cover/metro-boomin-the-weeknd-21-savage-creepin.jpeg",
            )
        )
    }

    fun getAllAudioBook(): LiveData<List<Audiobook>> {
        return repository.getAllAudioBook()
    }
}