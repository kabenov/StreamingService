package com.example.streamingservice.di

import com.example.streamingservice.data.API_BACKEND_BASE_URL
import com.example.streamingservice.data.API_SHAZAM_BASE_URL
import com.example.streamingservice.data.api_clients.ApiClientBackend
import com.example.streamingservice.data.StreamingServiceRepositoryImpl
import com.example.streamingservice.data.retrofit_clients.RetrofitClientBackend
import com.example.streamingservice.data.retrofit_clients.RetrofitClientShazam
import com.example.streamingservice.domain.StreamingServiceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

val dataModule: Module = module {

//    single {
//        Retrofit.Builder()
//            .baseUrl(API_BACKEND_BASE_URL)
//            .addConverterFactory(JacksonConverterFactory.create())
//            .build()
//    }
//
//    single {
//        get<Retrofit>().create(ApiClient::class.java)
//    }

    single {
        RetrofitClientBackend(API_BACKEND_BASE_URL).apiClient
    }

    single {
        RetrofitClientShazam(API_SHAZAM_BASE_URL).apiClient
    }

    single {
        StreamingServiceRepositoryImpl(
            apiClientBackend = get(),
            apiClientShazam = get(),
            context = androidContext()
        ) as StreamingServiceRepository
    }
}