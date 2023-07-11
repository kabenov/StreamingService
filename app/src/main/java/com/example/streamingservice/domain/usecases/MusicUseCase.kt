package com.example.streamingservice.domain.usecases

import androidx.lifecycle.LiveData
import com.example.streamingservice.domain.StreamingServiceRepository
import com.example.streamingservice.domain.entities.Genre
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist

class MusicUseCase(private val repository: StreamingServiceRepository) {

    fun getRecommendedMusics(): LiveData<List<Music>> {
//        val genre = Genre(
//            id = 1,
//            name = "Pop music"
//        )
//
//        return listOf(
//            Music(
//                id = 1,
//                title = "Creepin'",
//                author = "Metro Boomin, The Weeknd, 21 Savage",
//                genre = genre,
//                posterUri = "https://lmusic.kz/images/cover/metro-boomin-the-weeknd-21-savage-creepin.jpeg"
//            ),
//            Music(
//                id = 2,
//                title = "Creepin'",
//                author = "Metro Boomin, The Weeknd, 21 Savage",
//                genre = genre,
//                posterUri = "https://lmusic.kz/images/cover/metro-boomin-the-weeknd-21-savage-creepin.jpeg"
//            ),
//            Music(
//                id = 3,
//                title = "Creepin'",
//                author = "Metro Boomin, The Weeknd, 21 Savage",
//                genre = genre,
//                posterUri = "https://lmusic.kz/images/cover/metro-boomin-the-weeknd-21-savage-creepin.jpeg"
//            ),
//            Music(
//                id = 4,
//                title = "Creepin'",
//                author = "Metro Boomin, The Weeknd, 21 Savage",
//                genre = genre,
//                posterUri = "https://lmusic.kz/images/cover/metro-boomin-the-weeknd-21-savage-creepin.jpeg"
//            ),
//            Music(
//                id = 5,
//                title = "Creepin'",
//                author = "Metro Boomin, The Weeknd, 21 Savage",
//                genre = genre,
//                posterUri = "https://lmusic.kz/images/cover/metro-boomin-the-weeknd-21-savage-creepin.jpeg"
//            )
//        )

        return repository.getRecommendedMusics()
    }

    fun getAllPlaylist(): LiveData<List<Playlist>> {
        return repository.getAllPlaylist()
    }
}