package com.example.streamingservice.domain.usecases

import androidx.lifecycle.LiveData
import com.example.streamingservice.domain.StreamingServiceRepository
import com.example.streamingservice.domain.entities.Music

class SearchUseCase(private val repository: StreamingServiceRepository) {

    fun multiSearch(query: String): LiveData<List<Music>> {
        return repository.multiSearch(query)
    }
}