package com.example.finalprojectrickandmorty.database.characterDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characterRemoteKey: List<CharacterRemoteKey>)

    @Query("SELECT * FROM character_remote_keys WHERE characterId = :id")
    suspend fun remoteKeysCharacterId(id: Int?): CharacterRemoteKey?

    @Query("DELETE FROM character_remote_keys")
    suspend fun deleteAll()

}