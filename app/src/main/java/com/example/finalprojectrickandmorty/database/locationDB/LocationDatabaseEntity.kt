package com.example.finalprojectrickandmorty.database.locationDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "locations")
@TypeConverters(LocationTypeConverter::class)
data class LocationDatabaseEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "dimension")
    var dimension: String? = null,

    @ColumnInfo(name = "residents")
    var residents: ArrayList<String>? = arrayListOf(),

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "created")
    var created: String? = null

) : Serializable