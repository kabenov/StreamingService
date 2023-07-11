package com.example.streamingservice.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.streamingservice.domain.entities.SignInUpResponse
import com.example.streamingservice.domain.entities.SignUpRequest
import com.example.streamingservice.domain.usecases.UserUseCase

class SignUpViewModel(private val userUseCase: UserUseCase): ViewModel() {

    val registerUserLiveData: LiveData<SignInUpResponse> by lazy {
        userUseCase.getRegisterUserLiveData()
    }

    fun registerUser(signUpRequest: SignUpRequest) {
        userUseCase.registerUser(signUpRequest)
    }
}