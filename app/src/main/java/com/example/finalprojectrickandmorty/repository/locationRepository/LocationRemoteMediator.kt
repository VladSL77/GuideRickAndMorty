package com.example.finalprojectrickandmorty.repository.locationRepository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabase
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.database.locationDB.LocationRemoteKey
import com.example.finalprojectrickandmorty.network.locationNetwork.LocationNetwork
import com.example.finalprojectrickandmorty.util.Constants
import com.example.finalprojectrickandmorty.util.LocationResidentsList
import com.example.finalprojectrickandmorty.util.LocationSearchParams
import com.example.finalprojectrickandmorty.util.StateWarnings
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class LocationRemoteMediator @Inject constructor(
    private val locationNetwork: LocationNetwork,
    private val database: LocationDatabase,
) : RemoteMediator<Int, LocationDatabaseEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationDatabaseEntity>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        /*
        Тут требуется комментарий: всю логику ниже, ввиду многопоточности, пока не смог разобраться
        как разделить на отдельные методы для наглядности.
         */

        val nameForSearch = LocationSearchParams.name
        val typeForSearch = LocationSearchParams.type
        val dimensionForSearch = LocationSearchParams.dimension
        var response = ArrayList<LocationDatabaseEntity>(0)
        var isEndOfList: Boolean
        var stringIdForQuery = "[]"

        // Формирую строку с id для запроса в сеть

        if (LocationResidentsList.locationResidentsList.isNotEmpty()) {
            val listId = LocationResidentsList.locationResidentsList
            val result = buildString {
                for (item in listId) {
                    val last = item.substringAfterLast('/')
                    append("$last,")
                }
            }
            stringIdForQuery = "[${result.substringBeforeLast(",")}]"
        }

        // Работа с сетью

        try {
            if (stringIdForQuery == "[]") {
                val fullResponse = locationNetwork.getLocationNetwork(
                    page,
                    nameForSearch,
                    typeForSearch,
                    dimensionForSearch
                )
                response = fullResponse.results
                if (fullResponse.locationInfoNetwork?.next == null) isEndOfList = true
            } else {
                response = locationNetwork.getLocationNetworkById(stringIdForQuery)
                isEndOfList = true
            }
        } catch (e: Exception) {
            // Временное решение для проверки соединения, найти другое (проверка ping?)
            if (e !is retrofit2.HttpException) {
                StateWarnings.networkWarning = true
            }
        }

        // Работа с БД, формирование "ключей" для Медиатора

        return try {
            isEndOfList = response.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    if (response.isNotEmpty()) {
                        database.locationDao().deleteAll()
                        database.remoteKeyDao().deleteAll()
                    }
                }
                val prevKey: Int?
                val nextKey: Int?
                if (stringIdForQuery == "[]") {
                    prevKey = if (page == Constants.STARTING_PAGE_INDEX) null else page - 1
                    nextKey = if (isEndOfList) null else page + 1
                } else {
                    prevKey = null
                    nextKey = null
                }
                val keys = response.map {
                    LocationRemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao().insertAll(keys)
                database.locationDao().insertAll(response)
            }
            if (stringIdForQuery != "[]") isEndOfList = true
            MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, LocationDatabaseEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey
                    ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey =
                    remoteKeys?.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, LocationDatabaseEntity>): LocationRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeyDao().remoteKeysLocationId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, LocationDatabaseEntity>): LocationRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { episode -> database.remoteKeyDao().remoteKeysLocationId(episode.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, LocationDatabaseEntity>): LocationRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { episode -> database.remoteKeyDao().remoteKeysLocationId(episode.id) }
    }

}