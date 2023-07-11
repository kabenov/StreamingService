package com.example.streamingservice.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.streamingservice.data.StreamingServiceRepositoryImpl
import com.example.streamingservice.domain.entities.Audiobook
import com.example.streamingservice.domain.entities.Genre
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.usecases.AudiobookGenreUseCase
import com.example.streamingservice.domain.usecases.AudiobookUseCase

class AudiobookViewModel(
    private val repository: StreamingServiceRepositoryImpl,
    private val audiobookUseCase: AudiobookUseCase,
    private val audiobookGenreUseCase: AudiobookGenreUseCase
): ViewModel() {



    fun getAudiobookDataList(): List<Audiobook> {
        return audiobookUseCase.getRecommendedAudiobook()
    }

    fun getAllAudiobookDataList(): LiveData<List<Audiobook>> {
        return audiobookUseCase.getAllAudioBook()
    }
}