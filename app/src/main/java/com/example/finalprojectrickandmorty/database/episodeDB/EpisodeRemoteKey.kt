package com.example.finalprojectrickandmorty.database.episodeDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode_remote_keys")
data class EpisodeRemoteKey(

    @PrimaryKey
    val episodeId: Int?,
    val prevKey: Int?,
    val nextKey: Int?

)
