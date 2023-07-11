package com.example.streamingservice.data.retrofit_clients

import com.example.streamingservice.data.api_clients.ApiClientShazam
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitClientShazam(baseUrl: String) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    val apiClient: ApiClientShazam = retrofit.create(ApiClientShazam::class.java)
}