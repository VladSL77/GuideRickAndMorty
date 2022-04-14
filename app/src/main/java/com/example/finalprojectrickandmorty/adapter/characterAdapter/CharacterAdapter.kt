package com.example.finalprojectrickandmorty.adapter.characterAdapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity

class CharacterAdapter(private val clickListener: (CharacterDatabaseEntity) -> Unit) :
    PagingDataAdapter<CharacterDatabaseEntity, CharacterViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
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


}