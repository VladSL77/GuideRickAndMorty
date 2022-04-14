package com.example.finalprojectrickandmorty.database.characterDB

import androidx.room.TypeConverter

class CharacterTypeConverter {

    @TypeConverter
    fun fromListOrigin(characterOriginAndLocation: CharacterOriginAndLocation): String {
        return "${characterOriginAndLocation.id},${characterOriginAndLocation.name},${characterOriginAndLocation.url}"
    }

    @TypeConverter
    fun toListOrigin(string: String): CharacterOriginAndLocation {
        val data = ArrayList(string.split(","))
        return CharacterOriginAndLocation(data[0].toInt(), data[1], data[2])
    }

    @TypeConverter
    fun fromEpisode(episode: ArrayList<String>): String {
        return episode.joinToString(separator = ",")
    }

    @TypeConverter
    fun toEpisode(string: String): ArrayList<String> {
        return ArrayList(string.split(","))
    }

}