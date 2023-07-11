package com.example.streamingservice.di

import androidx.media3.exoplayer.ExoPlayer
import com.example.streamingservice.presentation.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val presentationModule: Module = module {

    single {
        ExoPlayer.Builder(
            androidContext()
        ).build()
    }

    viewModel {
        SignUpViewModel(
            userUseCase = get()
        )
    }

    viewModel {
        SignInViewModel(
            userUseCase = get()
        )
    }

    viewModel {
        HomePageViewModel(
            musicUseCase = get(),
            musicGenreUseCase = get()
        )
    }

    viewModel {
        AudiobookViewModel(
            repository = get(),
            audiobookUseCase = get(),
            audiobookGenreUseCase = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchViewModel = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            userUseCase = get()
        )
    }

    viewModel {
        UserProfileViewModel(
            userUseCase = get()
        )
    }
}