package com.example.streamingservice.di

import com.example.streamingservice.domain.usecases.*
import org.koin.core.module.Module
import org.koin.dsl.module

val domainModule: Module = module {

    factory {
        UserUseCase(repository = get())
    }

    factory {
        MusicUseCase(repository = get())
    }

    factory {
        MusicGenreUseCase(repository = get())
    }

    factory {
        AudiobookUseCase(repository = get())
    }

    factory {
        AudiobookGenreUseCase(repository = get())
    }

    factory {
        SearchUseCase(repository = get())
    }

}