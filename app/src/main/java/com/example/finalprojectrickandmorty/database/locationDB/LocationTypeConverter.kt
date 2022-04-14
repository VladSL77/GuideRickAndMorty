package com.example.finalprojectrickandmorty.database.locationDB

import androidx.room.TypeConverter

class LocationTypeConverter {


    @TypeConverter
    fun fromResident(resident: ArrayList<String>): String {
        return resident.joinToString(separator = ",")
    }

    @TypeConverter
    fun toResident(string: String): ArrayList<String> {
        return ArrayList(string.split(","))
    }

}