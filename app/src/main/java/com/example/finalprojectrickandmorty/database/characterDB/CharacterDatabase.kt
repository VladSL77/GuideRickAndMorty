package com.example.finalprojectrickandmorty.database.characterDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CharacterDatabaseEntity::class, CharacterOriginAndLocation::class, CharacterRemoteKey::class],
    version = 1
)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeyDao(): CharacterRemoteKeyDao

    companion object {
        const val CHARACTER_DATABASE_NAME = "character_database"
    }

}