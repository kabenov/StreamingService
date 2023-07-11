package com.example.streamingservice.domain.usecases

import androidx.lifecycle.LiveData
import com.example.streamingservice.domain.StreamingServiceRepository
import com.example.streamingservice.domain.entities.*

class UserUseCase(private val repository: StreamingServiceRepository) {

    fun registerUser(signUpRequest: SignUpRequest) {
        repository.registerUser(signUpRequest)
    }

    fun getRegisterUserLiveData(): LiveData<SignInUpResponse> {
        return repository.getRegisterUserLiveData()
    }

    fun signInUser(signInRequest: SignInRequest) {
        repository.signInUser(signInRequest)
    }

    fun getSignInUserLiveData(): LiveData<SignInUpResponse> {
        return repository.getSignInUserLiveData()
    }

    fun getCurrentUser(): User {
        return repository.getCurrentUser()
    }

    fun changeUserData(user: User): LiveData<SignInUpResponse> {
        return repository.changeUserData(user)
    }

    fun getUserPlaylist(): LiveData<List<Playlist>> {
        return repository.getUserPlaylist()
    }

    fun getUserFavorites(): LiveData<List<Music>> {
        return repository.getUserFavorites()
    }

}

