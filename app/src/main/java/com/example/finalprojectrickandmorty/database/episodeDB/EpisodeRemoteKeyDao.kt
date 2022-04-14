package com.example.finalprojectrickandmorty.database.episodeDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpisodeRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodeRemoteKey: List<EpisodeRemoteKey>)

    @Query("SELECT * FROM episode_remote_keys WHERE episodeId = :id")
    suspend fun remoteKeysEpisodeId(id: Int?): EpisodeRemoteKey?

    @Query("DELETE FROM episode_remote_keys")
    suspend fun deleteAll()

}