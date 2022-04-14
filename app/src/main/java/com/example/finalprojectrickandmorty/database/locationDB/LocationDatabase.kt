package com.example.finalprojectrickandmorty.database.locationDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationDatabaseEntity::class, LocationRemoteKey::class], version = 1)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun remoteKeyDao(): LocationRemoteKeyDao

    companion object {
        const val LOCATION_DATABASE_NAME = "location_database"
    }

}