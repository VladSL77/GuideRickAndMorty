package com.example.finalprojectrickandmorty.adapter.episodeAdapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity

class EpisodeAdapter(private val clickListener: (EpisodeDatabaseEntity) -> Unit) :
    PagingDataAdapter<EpisodeDatabaseEntity, EpisodeViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(clickListener, it) }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<EpisodeDatabaseEntity>() {
            override fun areItemsTheSame(
                oldItem: EpisodeDatabaseEntity,
                newItem: EpisodeDatabaseEntity
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: EpisodeDatabaseEntity,
                newItem: EpisodeDatabaseEntity
            ): Boolean = oldItem == newItem
        }
    }

}