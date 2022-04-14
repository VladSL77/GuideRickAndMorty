package com.example.finalprojectrickandmorty.database.characterDB

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterEntity: CharacterDatabaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterDatabaseEntity>)

    @Query("SELECT * FROM characters")
    fun getAll(): PagingSource<Int, CharacterDatabaseEntity>

    @Query(
        "SELECT * FROM characters WHERE" +
                " name LIKE '%'||:name||'%' AND" +
                " status LIKE '%'||:status||'%' AND" +
                " species LIKE '%'||:species||'%' AND" +
                " type LIKE '%'||:type||'%' AND" +
                " gender LIKE '%'||:gender||'%'"
    )
    fun getByFilter(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): PagingSource<Int, CharacterDatabaseEntity>

    @Query(
        "SELECT * FROM characters WHERE" +
                " name LIKE '%'||:name||'%' AND" +
                " status LIKE '%'||:status||'%' AND" +
                " species LIKE '%'||:species||'%' AND" +
                " type LIKE '%'||:type||'%' AND" +
                " gender LIKE '%'||:gender||'%'"
    )
    fun getByFilterForCheck(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): List<CharacterDatabaseEntity>

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}