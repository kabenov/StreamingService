package com.example.streamingservice.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.usecases.SearchUseCase

class SearchViewModel(private val searchViewModel: SearchUseCase) : ViewModel() {

    fun multiSearch(query: String): LiveData<List<Music>> {
        return searchViewModel.multiSearch(query)
    }
}