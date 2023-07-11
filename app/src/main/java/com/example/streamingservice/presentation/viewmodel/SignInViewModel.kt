package com.example.streamingservice.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.streamingservice.domain.entities.SignInRequest
import com.example.streamingservice.domain.entities.SignInUpResponse
import com.example.streamingservice.domain.usecases.UserUseCase

class SignInViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    val signInUserLiveData: LiveData<SignInUpResponse> by lazy {
        userUseCase.getSignInUserLiveData()
    }

    fun signInUser(signInRequest: SignInRequest) {
        userUseCase.signInUser(signInRequest)
    }
}