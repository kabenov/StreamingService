package com.example.streamingservice.presentation.view

import android.view.View
import android.view.ViewGroup
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist

class HomePlaylistCategoryAdapter(
    private val onMusicListener: OnMusicListener
): RecyclerView.Adapter<HomePlaylistCardViewHolder>() {

    private val dataList: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePlaylistCardViewHolder {
        return HomePlaylistCardViewHolder(View.inflate(parent.context, R.layout.music_card, null), onMusicListener)
    }

    override fun onBindViewHolder(holder: HomePlaylistCardViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(musicDataList: List<Playlist>) {
        dataList.clear()
        dataList.addAll(musicDataList)
        notifyDataSetChanged()
    }
}