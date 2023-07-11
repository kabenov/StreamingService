package com.example.streamingservice.domain.entities

import android.net.Uri
import androidx.core.net.toUri

data class User(
    val id: Long,
    val username: String,
    val email: String,
    var imgLink: String,
    val accessToken: String,
    var userFavoriteMusicsCount: Int = 0,
    var userPlaylistsCount: Int = 0,
    var imgLinkUri: Uri? = null,
)
