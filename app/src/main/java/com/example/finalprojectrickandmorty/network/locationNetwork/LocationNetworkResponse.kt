package com.example.finalprojectrickandmorty.network.locationNetwork

import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.google.gson.annotations.SerializedName

data class LocationNetworkResponse(

    @SerializedName("info")
    var locationInfoNetwork: LocationInfoNetwork? = LocationInfoNetwork(),
    @SerializedName("results")
    var results: ArrayList<LocationDatabaseEntity> = arrayListOf()

)
