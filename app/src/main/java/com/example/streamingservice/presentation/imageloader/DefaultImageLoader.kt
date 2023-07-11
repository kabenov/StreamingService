package com.example.streamingservice.presentation.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.streamingservice.R

class DefaultImageLoader : ImageLoader {

    override fun loadRecommendMusicPosterImg(context: Context, url: Uri, target: ImageView) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.music_card_img)
            .into(target)
    }

    override fun loadRecommendMusicPosterImg(context: Context, url: String, target: ImageView) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.music_card_img)
            .into(target)
    }

    override fun loadRecommendAudiobookPosterImg(context: Context, url: Uri, target: ImageView) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.audiobook_card_img)
            .into(target)
    }

    override fun loadRecommendAudiobookPosterImg(context: Context, url: String, target: ImageView) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.audiobook_card_img)
            .into(target)
    }

    override fun loadAlbumPosterImg(context: Context, url: Uri, target: ImageView) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.music_album_poster)
            .into(target)
    }

    override fun loadAlbumPosterImg(context: Context, url: String, target: ImageView) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.music_album_poster)
            .into(target)
    }
}