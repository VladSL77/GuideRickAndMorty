package com.example.finalprojectrickandmorty.database.locationDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_remote_keys")
data class LocationRemoteKey(

    @PrimaryKey
    val locationId: Int?,
    val prevKey: Int?,
    val nextKey: Int?

)
