package com.example.streamingservice.domain.entities

import java.io.Serializable

data class Playlist(
    val id: String,
    val playlistName: String,
    val imageLink: String,
    val musics: List<Music>
): Serializable
