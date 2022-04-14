package com.example.finalprojectrickandmorty.repository.characterRepository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabase
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.database.characterDB.CharacterRemoteKey
import com.example.finalprojectrickandmorty.network.characterNetwork.CharacterNetwork
import com.example.finalprojectrickandmorty.util.CharactersSearchParams
import com.example.finalprojectrickandmorty.util.Constants.STARTING_PAGE_INDEX
import com.example.finalprojectrickandmorty.util.EpisodeCharactersList
import com.example.finalprojectrickandmorty.util.LocationResidentsList
import com.example.finalprojectrickandmorty.util.StateWarnings
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator @Inject constructor(
    private val characterNetwork: CharacterNetwork,
    private val database: CharacterDatabase,
) : RemoteMediator<Int, CharacterDatabaseEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterDatabaseEntity>
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

        val nameForSearch = CharactersSearchParams.name
        val statusForSearch = CharactersSearchParams.status
        val speciesForSearch = CharactersSearchParams.species
        val typeForSearch = CharactersSearchParams.type
        val genderForSearch = CharactersSearchParams.gender
        var isEndOfList: Boolean
        var stringIdForQuery = "[]"
        var response = ArrayList<CharacterDatabaseEntity>(0)
        val listForEpisode = EpisodeCharactersList.episodeCharactersList
        val listForLocation = LocationResidentsList.locationResidentsList
        val listIdForDetails = ArrayList<String>()

        // Формирую строку с id для запроса в сеть

        if (listForEpisode.isNotEmpty() || listForLocation.isNotEmpty()) {
            listIdForDetails.addAll(listForEpisode)
            listIdForDetails.addAll(listForLocation)
            val result = buildString {
                for (item in listIdForDetails) {
                    val last = item.substringAfterLast('/')
                    append("$last,")
                }
            }
            stringIdForQuery = "[${result.substringBeforeLast(",")}]"
            listForEpisode.clear()
            listForLocation.clear()
            listIdForDetails.clear()
            EpisodeCharactersList.episodeCharactersList.clear()
            LocationResidentsList.locationResidentsList.clear()
        }

        // Работа с сетью

        try {
            if (stringIdForQuery == "[]") {
                val fullResponse = characterNetwork.getCharacterNetwork(
                    page,
                    nameForSearch,
                    statusForSearch,
                    speciesForSearch,
                    typeForSearch,
                    genderForSearch
                )
                response = fullResponse.results
                if (fullResponse.characterInfoNetwork?.next == null) isEndOfList = true
            } else {
                response = characterNetwork.getCharacterNetworkById(stringIdForQuery)
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
                        database.characterDao().deleteAll()
                        database.remoteKeyDao().deleteAll()
                    }
                }
                val prevKey: Int?
                val nextKey: Int?
                if (stringIdForQuery == "[]") {
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                    nextKey = if (isEndOfList) null else page + 1
                } else {
                    prevKey = null
                    nextKey = null
                }
                val keys = response.map {
                    CharacterRemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao().insertAll(keys)
                database.characterDao().insertAll(response)
            }
            if (stringIdForQuery != "[]") isEndOfList = true
            MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, CharacterDatabaseEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey
                    ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterDatabaseEntity>): CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeyDao().remoteKeysCharacterId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterDatabaseEntity>): CharacterRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character -> database.remoteKeyDao().remoteKeysCharacterId(character.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, CharacterDatabaseEntity>): CharacterRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character -> database.remoteKeyDao().remoteKeysCharacterId(character.id) }
    }

}