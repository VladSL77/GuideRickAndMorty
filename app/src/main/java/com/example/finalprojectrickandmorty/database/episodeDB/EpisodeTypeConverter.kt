package com.example.finalprojectrickandmorty.database.episodeDB

import androidx.room.TypeConverter

class EpisodeTypeConverter {

    @TypeConverter
    fun fromCharacter(character: ArrayList<String>): String {
        return character.joinToString(separator = ",")
    }

    @TypeConverter
    fun toCharacter(string: String): ArrayList<String> {
        return ArrayList(string.split(","))
    }

}