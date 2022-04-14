package com.example.finalprojectrickandmorty.adapter.locationAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.CharacterMainItemBinding

class LocationResidentAdapter(private val clickListener: (CharacterDatabaseEntity) -> Unit) :
    PagingDataAdapter<CharacterDatabaseEntity, LocationResidentAdapter.LocationDetailsViewHolder>(
        COMPARATOR
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationDetailsViewHolder {
        return LocationDetailsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LocationDetailsViewHolder, position: Int) {
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

    class LocationDetailsViewHolder(
        private val binding: CharacterMainItemBinding,
        private val v: ViewGroup
    ) :
        RecyclerView.ViewHolder(binding.root) {

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
            fun create(v: ViewGroup): LocationDetailsViewHolder {
                val inflater = LayoutInflater.from(v.context)
                val binding = CharacterMainItemBinding.inflate(inflater, v, false)
                return LocationDetailsViewHolder(binding, v)
            }
        }

    }

}