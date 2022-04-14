package com.example.finalprojectrickandmorty.network.episodeNetwork

import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.google.gson.annotations.SerializedName

data class EpisodeNetworkResponse(

    @SerializedName("info")
    var episodeInfoNetwork: EpisodeInfoNetwork? = EpisodeInfoNetwork(),
    @SerializedName("results")
    var results: ArrayList<EpisodeDatabaseEntity> = arrayListOf()

)
