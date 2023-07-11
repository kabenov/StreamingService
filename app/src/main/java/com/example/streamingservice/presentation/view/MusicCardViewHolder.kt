package com.example.streamingservice.presentation.view

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.presentation.imageloader.DefaultImageLoader
import com.example.streamingservice.presentation.imageloader.ImageLoader

class MusicCardViewHolder(
    itemView: View,
    private val onMusicListener: OnMusicListener,
    private val audioPlayer: ExoPlayer
) : RecyclerView.ViewHolder(itemView) {

    private val posterMusicImageView: ImageView =
        itemView.findViewById(R.id.music_card_image_view_music_poster)
    private val nameMusicTextView: TextView =
        itemView.findViewById(R.id.music_card_text_view_music_name)
    private val authorMusicTextView: TextView =
        itemView.findViewById(R.id.music_card_text_view_music_author)

    private var dataList: MutableList<Music> = mutableListOf()

    private val imageLoader: ImageLoader = DefaultImageLoader()

    fun onBind(music: Music, position: Int, dataList: MutableList<Music>) {
        nameMusicTextView.text = music.title
        authorMusicTextView.text = music.author
        imageLoader.loadRecommendMusicPosterImg(
            context = posterMusicImageView.context,
            url = music.posterUri,
            target = posterMusicImageView
        )
        this.dataList = dataList

        itemView.setOnClickListener {
            val mediaItem: MediaItem = getMediaItem(music)
            if (!audioPlayer.isPlaying) {
                audioPlayer.setMediaItems(getMediaItems(), position, 0)
            } else {
                audioPlayer.pause()
                audioPlayer.seekTo(position, 0)
            }
            audioPlayer.prepare()
            audioPlayer.play()
            onMusicListener.onMusicClick(music.id)
        }
    }

    private fun getMediaItems(): List<MediaItem> {
        val mediaItems: MutableList<MediaItem> = ArrayList()
        for (music in dataList) {
            val mediaItem = getMediaItem(music)
            mediaItems.add(mediaItem)
        }
        return mediaItems
    }

    private fun getMediaItem(music: Music): MediaItem {
        return MediaItem.Builder()
            .setUri(music.uri)
            .setMediaMetadata(getMetadata(music))
            .build()
    }

    private fun getMetadata(music: Music): MediaMetadata {
        return MediaMetadata.Builder()
            .setTitle(music.title)
            .setArtist(music.author)
            .setGenre(music.genre.name)
            .setArtworkUri(Uri.parse(music.posterUri))
            .build()
    }
}