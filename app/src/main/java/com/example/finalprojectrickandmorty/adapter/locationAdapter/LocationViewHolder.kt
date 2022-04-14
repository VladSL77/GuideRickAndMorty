package com.example.finalprojectrickandmorty.adapter.locationAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.LocationMainItemBinding

class LocationViewHolder(private val binding: LocationMainItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: (LocationDatabaseEntity) -> Unit, item: LocationDatabaseEntity) {
        binding.textName.text = item.name
        binding.textType.text = item.type
        binding.textDimension.text = item.dimension
        binding.root.setOnClickListener {
            clickListener(item)
        }
    }

    companion object {
        fun create(v: ViewGroup): LocationViewHolder {
            val inflater = LayoutInflater.from(v.context)
            val binding = LocationMainItemBinding.inflate(inflater, v, false)
            return LocationViewHolder(binding)
        }
    }

}