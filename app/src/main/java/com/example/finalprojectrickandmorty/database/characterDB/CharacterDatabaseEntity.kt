package com.example.finalprojectrickandmorty.database.characterDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "characters")
@TypeConverters(CharacterTypeConverter::class)
data class CharacterDatabaseEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "species")
    var species: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "gender")
    var gender: String? = null,

    @ColumnInfo(name = "origin")
    var origin: CharacterOriginAndLocation? = null,

    @ColumnInfo(name = "location")
    var location: CharacterOriginAndLocation? = null,

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "episode")
    var episode: ArrayList<String>? = arrayListOf(),

    @ColumnInfo(name = "url")
    var url: String? = null

) : Serializable
