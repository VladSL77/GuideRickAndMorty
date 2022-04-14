package com.example.finalprojectrickandmorty.adapter.episodeAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.CharacterMainItemBinding

class EpisodeCharacterAdapter(private val clickListener: (CharacterDatabaseEntity) -> Unit) :
    PagingDataAdapter<CharacterDatabaseEntity, EpisodeCharacterAdapter.EpisodeDetailsViewHolder>(
        COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeDetailsViewHolder {
        return EpisodeDetailsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EpisodeDetailsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(clickListener, it) }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CharacterDatabaseEntity>() {
            override fun areItemsTheSame(
                oldItem: CharacterDatabaseEntity,
                newItem: CharacterDatabaseEntity
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CharacterDatabaseEntity,
                newItem: CharacterDatabaseEntity
            ): Boolean = oldItem == newItem
        }
    }

    class EpisodeDetailsViewHolder(
        private val binding: CharacterMainItemBinding,
        private val v: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: (CharacterDatabaseEntity) -> Unit, item: CharacterDatabaseEntity) {
            binding.textName.text = item.name
            binding.textSpecies.text = item.species
            binding.textStatus.text = item.status
            binding.textGender.text = item.gender
            Glide.with(v).load(item.image).into(binding.imageCharacter)
            binding.root.setOnClickListener {
                clickListener(item)
            }
        }

        companion object {
            fun create(v: ViewGroup): EpisodeDetailsViewHolder {
                val inflater = LayoutInflater.from(v.context)
                val binding = CharacterMainItemBinding.inflate(inflater, v, false)
                return EpisodeDetailsViewHolder(binding, v)
            }
        }

    }

}