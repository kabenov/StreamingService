package com.example.streamingservice.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist
import com.example.streamingservice.domain.entities.User
import com.example.streamingservice.domain.usecases.UserUseCase

class FavoritesViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    val currentUser: User by lazy {
        userUseCase.getCurrentUser()
    }

    fun getUserPlaylist(): LiveData<List<Playlist>> {
        return userUseCase.getUserPlaylist()
    }

    fun getUserFavorites(): LiveData<List<Music>> {
        return userUseCase.getUserFavorites()
    }
}