package com.example.streamingservice.data.dbmodels

import android.net.Uri

data class UserUpdateDbModel(
    val id: Long,
    val username: String,
    val email: String,
    val img_link: String?,
    val subscribed: Boolean = false
)
