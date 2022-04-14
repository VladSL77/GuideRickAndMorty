package com.example.finalprojectrickandmorty.database.episodeDB

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpisodeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episodeEntity: EpisodeDatabaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeDatabaseEntity>)

    @Query("SELECT * FROM episodes")
    fun getAll(): PagingSource<Int, EpisodeDatabaseEntity>

    @Query(
        "SELECT * FROM episodes WHERE" +
                " name LIKE '%'||:name||'%' AND" +
                " episode LIKE '%'||:episode||'%' AND" +
                " airDate LIKE '%'||:airDate||'%'"
    )
    fun getByFilter(
        name: String?,
        episode: String?,
        airDate: String?
    ): PagingSource<Int, EpisodeDatabaseEntity>

    @Query(
        "SELECT * FROM episodes WHERE" +
                " name LIKE '%'||:name||'%' AND" +
                " episode LIKE '%'||:episode||'%' AND" +
                " airDate LIKE '%'||:airDate||'%'"
    )
    fun getByFilterForCheck(
        name: String?,
        episode: String?,
        airDate: String?
    ): List<EpisodeDatabaseEntity>

    @Query("DELETE FROM episodes")
    suspend fun deleteAll()

}