package com.example.finalprojectrickandmorty.network.episodeNetwork

import com.google.gson.annotations.SerializedName

data class EpisodeInfoNetwork(

    @SerializedName("count")
    var count: Int? = null,
    @SerializedName("pages")
    var pages: Int? = null,
    @SerializedName("next")
    var next: String? = null,
    @SerializedName("prev")
    var prev: String? = null

)