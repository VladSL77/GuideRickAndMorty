package com.example.finalprojectrickandmorty.network.episodeNetwork

import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeNetwork {

    @GET("episode")
    suspend fun getEpisodeNetwork(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("episode") episode: String,
        @Query("air_date") airDate: String
    ): EpisodeNetworkResponse

    @GET("episode/{id}")
    suspend fun getEpisodeNetworkById(
        @Path("id")
        id: String
    ): ArrayList<EpisodeDatabaseEntity>

}