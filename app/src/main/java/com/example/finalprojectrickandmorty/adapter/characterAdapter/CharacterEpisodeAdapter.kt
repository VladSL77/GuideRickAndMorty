package com.example.finalprojectrickandmorty.adapter.characterAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.CharacterEpisodeItemBinding

class CharacterEpisodeAdapter(private val clickListener: (EpisodeDatabaseEntity) -> Unit) :
    PagingDataAdapter<EpisodeDatabaseEntity, CharacterEpisodeAdapter.CharacterDetailsViewHolder>(
        COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterDetailsViewHolder {
        return CharacterDetailsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharacterDetailsViewHolder, position: Int) {
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

    class CharacterDetailsViewHolder(private val binding: CharacterEpisodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: (EpisodeDatabaseEntity) -> Unit, item: EpisodeDatabaseEntity) {
            binding.textCharacterEpisodeItem.text = item.name
            binding.root.setOnClickListener {
                clickListener(item)
            }
        }

        companion object {
            fun create(v: ViewGroup): CharacterDetailsViewHolder {
                val inflater = LayoutInflater.from(v.context)
                val binding = CharacterEpisodeItemBinding.inflate(inflater, v, false)
                return CharacterDetailsViewHolder(binding)
            }
        }

    }

}