package com.example.streamingservice.presentation.view

import android.view.View
import android.view.ViewGroup
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Music

class MusicCategoryAdapter(
    private val onMusicListener: OnMusicListener,
    private val audioPlayer: ExoPlayer
): RecyclerView.Adapter<MusicCardViewHolder>() {

    private val dataList: MutableList<Music> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicCardViewHolder {
        return MusicCardViewHolder(View.inflate(parent.context, R.layout.music_card, null), onMusicListener, audioPlayer)
    }

    override fun onBindViewHolder(holder: MusicCardViewHolder, position: Int) {
        holder.onBind(dataList[position], position, dataList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(musicDataList: List<Music>) {
        dataList.clear()
        dataList.addAll(musicDataList)
        notifyDataSetChanged()
    }
}