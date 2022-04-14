package com.example.finalprojectrickandmorty.database.locationDB

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationEntity: LocationDatabaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationDatabaseEntity>)

    @Query("SELECT * FROM locations")
    fun getAll(): PagingSource<Int, LocationDatabaseEntity>

    @Query(
        "SELECT * FROM locations WHERE" +
                " name LIKE '%'||:name||'%' AND" +
                " type LIKE '%'||:type||'%' AND" +
                " dimension LIKE '%'||:dimension||'%'"
    )
    fun getByFilter(
        name: String?,
        type: String?,
        dimension: String?
    ): PagingSource<Int, LocationDatabaseEntity>

    @Query(
        "SELECT * FROM locations WHERE" +
                " name LIKE '%'||:name||'%' AND" +
                " type LIKE '%'||:type||'%' AND" +
                " dimension LIKE '%'||:dimension||'%'"
    )
    fun getByFilterForCheck(
        name: String?,
        type: String?,
        dimension: String?
    ): List<LocationDatabaseEntity>

    @Query("SELECT * FROM locations WHERE id LIKE :id")
    fun getSingleLocationById(
        id: Int?
    ): LocationDatabaseEntity

    @Query("DELETE FROM locations")
    suspend fun deleteAll()

}