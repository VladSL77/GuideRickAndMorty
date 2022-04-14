package com.example.finalprojectrickandmorty.network.characterNetwork

import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterNetwork {

    @GET("character")
    suspend fun getCharacterNetwork(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender: String,
    ): CharactersNetworkResponse

    @GET("character/{id}")
    suspend fun getCharacterNetworkById(
        @Path("id")
        id: String
    ): ArrayList<CharacterDatabaseEntity>

}