package com.example.streamingservice.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.streamingservice.data.StreamingServiceRepositoryImpl
import com.example.streamingservice.domain.entities.Genre
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist
import com.example.streamingservice.domain.usecases.MusicGenreUseCase
import com.example.streamingservice.domain.usecases.MusicUseCase

class HomePageViewModel(
    private val musicUseCase: MusicUseCase,
    private val musicGenreUseCase: MusicGenreUseCase
): ViewModel() {

    fun getMusicDataList(): LiveData<List<Music>> {
        return musicUseCase.getRecommendedMusics()
    }

    fun getGenreDataList(): List<Genre> {
        return musicGenreUseCase.getMusicGenres()
    }

    fun getAllPlaylist(): LiveData<List<Playlist>> {
        return musicUseCase.getAllPlaylist()
    }
}