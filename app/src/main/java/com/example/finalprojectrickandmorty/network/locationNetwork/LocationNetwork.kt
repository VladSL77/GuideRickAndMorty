package com.example.finalprojectrickandmorty.network.locationNetwork

import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationNetwork {

    @GET("location")
    suspend fun getLocationNetwork(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("dimension") dimension: String
    ): LocationNetworkResponse

    @GET("location/{id}")
    suspend fun getLocationNetworkById(
        @Path("id")
        id: String
    ): ArrayList<LocationDatabaseEntity>

    @GET("location/{id}")
    suspend fun getSingleLocationNetworkById(
        @Path("id")
        id: String
    ): LocationDatabaseEntity

}