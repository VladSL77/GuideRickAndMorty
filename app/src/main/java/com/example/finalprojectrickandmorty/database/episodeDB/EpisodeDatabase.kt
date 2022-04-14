package com.example.finalprojectrickandmorty.database.episodeDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EpisodeDatabaseEntity::class, EpisodeRemoteKey::class], version = 1)
abstract class EpisodeDatabase : RoomDatabase() {

    abstract fun episodeDao(): EpisodeDao
    abstract fun remoteKeyDao(): EpisodeRemoteKeyDao

    companion object {
        const val EPISODE_DATABASE_NAME = "episode_database"
    }

}