package com.example.streamingservice.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.streamingservice.domain.entities.SignInUpResponse
import com.example.streamingservice.domain.entities.User
import com.example.streamingservice.domain.usecases.UserUseCase

class UserProfileViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    val currentUser: User by lazy {
        userUseCase.getCurrentUser()
    }

    fun changeUserData(user: User): LiveData<SignInUpResponse> {
        return userUseCase.changeUserData(user)
    }
}