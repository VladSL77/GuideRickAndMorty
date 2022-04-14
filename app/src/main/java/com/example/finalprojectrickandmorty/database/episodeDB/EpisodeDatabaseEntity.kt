package com.example.finalprojectrickandmorty.database.episodeDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable


@Entity(tableName = "episodes")
@TypeConverters(EpisodeTypeConverter::class)
data class EpisodeDatabaseEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "airDate")
    var air_date: String? = null,

    @ColumnInfo(name = "episode")
    var episode: String? = null,

    @ColumnInfo(name = "characters")
    var characters: ArrayList<String>? = arrayListOf(),

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "created")
    var created: String? = null

) : Serializable
