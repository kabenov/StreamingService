package com.example.streamingservice.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist
import com.example.streamingservice.presentation.imageloader.DefaultImageLoader
import com.example.streamingservice.presentation.imageloader.ImageLoader
import com.google.android.material.imageview.ShapeableImageView

class PlaylistCardViewHolder(
    itemView: View,
    private val onMusicListener: OnMusicListener
) : RecyclerView.ViewHolder(itemView) {

    private val posterPlaylistImageView: ShapeableImageView =
        itemView.findViewById(R.id.image_view_playlist_card_poster)
    private val titlePlaylistTextView: TextView =
        itemView.findViewById(R.id.text_view_playlist_card_title)
    private val playlistMusicSumTextView: TextView =
        itemView.findViewById(R.id.text_view_playlist_card_music_count)

    private val imageLoader: ImageLoader = DefaultImageLoader()

    fun onBind(playlist: Playlist) {
        titlePlaylistTextView.text = playlist.playlistName
        playlistMusicSumTextView.text = playlist.musics.size.toString() + " әуен"

        imageLoader.loadRecommendMusicPosterImg(
            context = posterPlaylistImageView.context,
            url = playlist.imageLink,
            target = posterPlaylistImageView
        )

        itemView.setOnClickListener {
            onMusicListener.onMusicClick(playlist.id)
        }
    }
}