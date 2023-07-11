package com.example.streamingservice.domain

import androidx.lifecycle.LiveData
import com.example.streamingservice.domain.entities.*

interface StreamingServiceRepository {

    fun registerUser(signUpRequest: SignUpRequest)

    fun getRegisterUserLiveData(): LiveData<SignInUpResponse>

    fun signInUser(signInRequest: SignInRequest)

    fun getSignInUserLiveData(): LiveData<SignInUpResponse>

    fun getCurrentUser(): User

    fun changeUserData(user: User): LiveData<SignInUpResponse>

    fun getRecommendedMusics(): LiveData<List<Music>>

    fun getMusicById(musicId: Int)

    fun getRecommendedAudiobooks()

    fun getAudiobookById()

    fun getMusicGenres()

    fun getAudiobookGenres()

    fun getMusicsByGenre(genre: Genre)

    fun getNewAlbums()

    fun getMusicsByName(name: String)
    fun getMusicsByAuthor(author: String)
    fun getAlbumsByName(name: String)
    fun getAlbumsByAuthor(author: String)
    fun getAudiobooksByName(name: String)
    fun getAudiobooksByAuthor(author: String)


    fun multiSearch(query: String): LiveData<List<Music>>

    fun getUserPlaylist(): LiveData<List<Playlist>>

    fun getUserFavorites(): LiveData<List<Music>>

    fun getAllAudioBook(): LiveData<List<Audiobook>>

    fun getAllPlaylist(): LiveData<List<Playlist>>
}