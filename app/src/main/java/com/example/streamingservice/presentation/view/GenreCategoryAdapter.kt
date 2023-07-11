package com.example.streamingservice.presentation.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.streamingservice.R
import com.example.streamingservice.domain.entities.Genre

class GenreCategoryAdapter(
    private val onGenreListener: OnGenreListener
) : RecyclerView.Adapter<GenreCardViewHolder>() {

    private val dataList: MutableList<Genre> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreCardViewHolder {
        return GenreCardViewHolder(
            View.inflate(parent.context, R.layout.genre_card, null),
            onGenreListener
        )
    }

    override fun onBindViewHolder(holder: GenreCardViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(genreDataList: List<Genre>) {
        dataList.clear()
        dataList.addAll(genreDataList)
        notifyDataSetChanged()
    }
}