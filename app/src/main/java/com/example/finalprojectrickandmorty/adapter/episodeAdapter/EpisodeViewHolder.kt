package com.example.finalprojectrickandmorty.adapter.episodeAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.EpisodeMainItemBinding

class EpisodeViewHolder(private val binding: EpisodeMainItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: (EpisodeDatabaseEntity) -> Unit, item: EpisodeDatabaseEntity) {
        binding.textName.text = item.name
        binding.textEpisode.text = item.episode
        binding.textAirData.text = item.air_date
        binding.root.setOnClickListener {
            clickListener(item)
        }
    }

    companion object {
        fun create(v: ViewGroup): EpisodeViewHolder {
            val inflater = LayoutInflater.from(v.context)
            val binding = EpisodeMainItemBinding.inflate(inflater, v, false)
            return EpisodeViewHolder(binding)
        }
    }

}