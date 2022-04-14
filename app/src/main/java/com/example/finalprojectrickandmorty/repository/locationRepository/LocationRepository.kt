package com.example.finalprojectrickandmorty.repository.locationRepository

import androidx.paging.*
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabase
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.network.locationNetwork.LocationNetwork
import com.example.finalprojectrickandmorty.util.LocationSearchParams
import com.example.finalprojectrickandmorty.util.StateWarnings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationNetwork: LocationNetwork,
    private val database: LocationDatabase,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getLocationFromMediator(): Flow<PagingData<LocationDatabaseEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            remoteMediator = LocationRemoteMediator(
                locationNetwork,
                database
            ),
            pagingSourceFactory = {
                createPagingSourceFactory()
            }
        ).flow
    }

    /*
    Данный метод оставлю на будущее

    suspend fun getSingleLocation(): LocationDatabaseEntity {
        var idLocation = ""
        var location = LocationDatabaseEntity()
        if (CharacterOriginAndLocationList.originAndLocation.isNotEmpty()) {
            idLocation =
                CharacterOriginAndLocationList.originAndLocation.substringAfterLast('/')
        }
        try {
            val response = locationNetwork.getSingleLocationNetworkById(idLocation)
            database.locationDao().insert(response)
            withContext(Dispatchers.IO) {
                location = database.locationDao().getSingleLocationById(idLocation.toInt())
            }
        } catch (e: Exception) {
            StateWarnings.networkWarning = true
            e.printStackTrace()
        }
        return location
    }
     */


    private fun createPagingSourceFactory(): PagingSource<Int, LocationDatabaseEntity> {
        val name = LocationSearchParams.name
        val type = LocationSearchParams.type
        val dimension = LocationSearchParams.dimension
        val responseDatabase: PagingSource<Int, LocationDatabaseEntity>
        if (name != "" || type != "" || dimension != "") {
            responseDatabase = database.locationDao().getByFilter(name, type, dimension)
            checkQueryForEmpty(name, type, dimension)
        } else {
            responseDatabase = database.locationDao().getAll()
        }
        return responseDatabase
    }

    private fun checkQueryForEmpty(
        name: String,
        type: String,
        dimension: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val check = database.locationDao().getByFilterForCheck(name, type, dimension)
            if (check.isEmpty()) {
                StateWarnings.emptyDataWarning = true
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}