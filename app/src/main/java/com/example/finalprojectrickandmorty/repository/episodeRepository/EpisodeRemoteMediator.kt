package com.example.finalprojectrickandmorty.repository.episodeRepository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabase
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeRemoteKey
import com.example.finalprojectrickandmorty.network.episodeNetwork.EpisodeNetwork
import com.example.finalprojectrickandmorty.util.CharacterEpisodesList
import com.example.finalprojectrickandmorty.util.Constants.STARTING_PAGE_INDEX
import com.example.finalprojectrickandmorty.util.EpisodesSearchParams
import com.example.finalprojectrickandmorty.util.StateWarnings
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EpisodeRemoteMediator @Inject constructor(
    private val episodeNetwork: EpisodeNetwork,
    private val database: EpisodeDatabase,
) : RemoteMediator<Int, EpisodeDatabaseEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeDatabaseEntity>
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

        val nameForSearch = EpisodesSearchParams.name
        val episodeForSearch = EpisodesSearchParams.episode
        val airDataForSearch = EpisodesSearchParams.airDate
        var response = ArrayList<EpisodeDatabaseEntity>(0)
        var isEndOfList: Boolean
        var stringIdForQuery = "[]"

        // Формирую строку с id для запроса в сеть

        if (CharacterEpisodesList.characterEpisodesList.isNotEmpty()) {
            val listId = CharacterEpisodesList.characterEpisodesList
            val result = buildString {
                for (item in listId) {
                    val last = item.substringAfterLast('/')
                    append("$last,")
                }
            }
            stringIdForQuery = "[${result.substringBeforeLast(",")}]"
            CharacterEpisodesList.characterEpisodesList.clear()
        }

        // Работа с сетью

        try {
            if (stringIdForQuery == "[]") {
                val fullResponse = episodeNetwork.getEpisodeNetwork(
                    page,
                    nameForSearch,
                    episodeForSearch,
                    airDataForSearch
                )
                response = fullResponse.results
                if (fullResponse.episodeInfoNetwork?.next == null) isEndOfList = true
            } else {
                response = episodeNetwork.getEpisodeNetworkById(stringIdForQuery)
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
                    database.episodeDao().deleteAll()
                    database.remoteKeyDao().deleteAll()
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
                    EpisodeRemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao().insertAll(keys)
                database.episodeDao().insertAll(response)
                Log.i("TAG", "Char Mediator KEYS $prevKey - $page - $nextKey")
            }
            if (stringIdForQuery != "[]") isEndOfList = true
            MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, EpisodeDatabaseEntity>
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
                val prevKey =
                    remoteKeys?.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, EpisodeDatabaseEntity>): EpisodeRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeyDao().remoteKeysEpisodeId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, EpisodeDatabaseEntity>): EpisodeRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { episode -> database.remoteKeyDao().remoteKeysEpisodeId(episode.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, EpisodeDatabaseEntity>): EpisodeRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { episode -> database.remoteKeyDao().remoteKeysEpisodeId(episode.id) }
    }

}