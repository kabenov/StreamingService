package com.example.streamingservice.presentation.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Genre

class GenreCardViewHolder(
    itemView: View,
    private val onGenreListener: OnGenreListener
) : RecyclerView.ViewHolder(itemView) {

    private val nameMusicTextView: TextView =
        itemView.findViewById(R.id.genre_card_text_view_genre_name)

    fun onBind(genre: Genre) {
        nameMusicTextView.text = genre.name

        itemView.setOnClickListener {
            onGenreListener.onGenreClick(genre.id)
        }
    }
}