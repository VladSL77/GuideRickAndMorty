package com.example.finalprojectrickandmorty.database.locationDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locationRemoteKey: List<LocationRemoteKey>)

    @Query("SELECT * FROM location_remote_keys WHERE locationId = :id")
    suspend fun remoteKeysLocationId(id: Int?): LocationRemoteKey?

    @Query("DELETE FROM location_remote_keys")
    suspend fun deleteAll()

}