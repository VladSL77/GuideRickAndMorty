package com.example.finalprojectrickandmorty.adapter.locationAdapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity

class LocationAdapter(private val clickListener: (LocationDatabaseEntity) -> Unit) :
    PagingDataAdapter<LocationDatabaseEntity, LocationViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(clickListener, it) }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<LocationDatabaseEntity>() {
            override fun areItemsTheSame(
                oldItem: LocationDatabaseEntity,
                newItem: LocationDatabaseEntity
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LocationDatabaseEntity,
                newItem: LocationDatabaseEntity
            ): Boolean = oldItem == newItem
        }
    }

}