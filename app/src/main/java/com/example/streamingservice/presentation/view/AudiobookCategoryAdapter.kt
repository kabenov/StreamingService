package com.example.streamingservice.presentation.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Audiobook

class AudiobookCategoryAdapter(
    private val onMusicListener: OnMusicListener
): RecyclerView.Adapter<AudiobookCardViewHolder>() {

    private val dataList: MutableList<Audiobook> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudiobookCardViewHolder {
        return AudiobookCardViewHolder(View.inflate(parent.context, R.layout.music_card, null), onMusicListener)
    }

    override fun onBindViewHolder(holder: AudiobookCardViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(musicDataList: List<Audiobook>) {
        dataList.clear()
        dataList.addAll(musicDataList)
        notifyDataSetChanged()
    }
}