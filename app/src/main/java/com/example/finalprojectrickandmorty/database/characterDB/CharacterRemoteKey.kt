package com.example.finalprojectrickandmorty.database.characterDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_remote_keys")
data class CharacterRemoteKey(

    @PrimaryKey
    val characterId: Int?,
    val prevKey: Int?,
    val nextKey: Int?

)
