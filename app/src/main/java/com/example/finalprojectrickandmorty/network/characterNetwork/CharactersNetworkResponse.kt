package com.example.finalprojectrickandmorty.network.characterNetwork

import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.google.gson.annotations.SerializedName


data class CharactersNetworkResponse(

    @SerializedName("info")
    var characterInfoNetwork: CharacterInfoNetwork? = CharacterInfoNetwork(),
    @SerializedName("results")
    var results: ArrayList<CharacterDatabaseEntity> = arrayListOf()

)