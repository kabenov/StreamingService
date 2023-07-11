package com.example.streamingservice.presentation.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView

interface ImageLoader {

    fun loadRecommendMusicPosterImg(
        context: Context,
        url: Uri,
        target: ImageView
    )

    fun loadRecommendMusicPosterImg(
        context: Context,
        url: String,
        target: ImageView
    )

    fun loadRecommendAudiobookPosterImg(
        context: Context,
        url: Uri,
        target: ImageView
    )

    fun loadRecommendAudiobookPosterImg(
        context: Context,
        url: String,
        target: ImageView
    )

    fun loadAlbumPosterImg(
        context: Context,
        url: Uri,
        target: ImageView
    )

    fun loadAlbumPosterImg(
        context: Context,
        url: String,
        target: ImageView
    )
}