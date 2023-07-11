package com.example.streamingservice.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Audiobook
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.presentation.imageloader.DefaultImageLoader
import com.example.streamingservice.presentation.imageloader.ImageLoader

class AudiobookCardViewHolder(
    itemView: View,
    private val onMusicListener: OnMusicListener
) : RecyclerView.ViewHolder(itemView) {

    private val posterMusicImageView: ImageView =
        itemView.findViewById(R.id.music_card_image_view_music_poster)
    private val nameMusicTextView: TextView =
        itemView.findViewById(R.id.music_card_text_view_music_name)
    private val authorMusicTextView: TextView =
        itemView.findViewById(R.id.music_card_text_view_music_author)

    private val imageLoader: ImageLoader = DefaultImageLoader()

    fun onBind(audiobook: Audiobook) {
        nameMusicTextView.text = audiobook.name
        authorMusicTextView.text = audiobook.author
        imageLoader.loadRecommendAudiobookPosterImg(
            context = posterMusicImageView.context,
            url = audiobook.posterUrl,
            target = posterMusicImageView
        )

        itemView.setOnClickListener {
            onMusicListener.onMusicClick(audiobook.id)
        }
    }
}