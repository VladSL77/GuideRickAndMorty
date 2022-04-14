package com.example.finalprojectrickandmorty.adapter.characterAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.CharacterMainItemBinding

class CharacterViewHolder(private val binding: CharacterMainItemBinding, private val v: ViewGroup) :
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
        fun create(v: ViewGroup): CharacterViewHolder {
            val inflater = LayoutInflater.from(v.context)
            val binding = CharacterMainItemBinding.inflate(inflater, v, false)
            return CharacterViewHolder(binding, v)
        }
    }

}