package com.example.streamingservice.data.retrofit_clients

import com.example.streamingservice.data.api_clients.ApiClientBackend
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitClientBackend(baseUrl: String) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    val apiClient: ApiClientBackend = retrofit.create(ApiClientBackend::class.java)
}