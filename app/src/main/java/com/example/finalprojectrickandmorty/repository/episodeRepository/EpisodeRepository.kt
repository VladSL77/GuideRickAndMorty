package com.example.finalprojectrickandmorty.repository.episodeRepository

import androidx.paging.*
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabase
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.network.episodeNetwork.EpisodeNetwork
import com.example.finalprojectrickandmorty.util.EpisodesSearchParams
import com.example.finalprojectrickandmorty.util.StateWarnings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val episodeNetwork: EpisodeNetwork,
    private val database: EpisodeDatabase,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getEpisodeFromMediator(): Flow<PagingData<EpisodeDatabaseEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            remoteMediator = EpisodeRemoteMediator(
                episodeNetwork,
                database
            ),
            pagingSourceFactory = {
                createPagingSourceFactory()
            }
        ).flow
    }

    private fun createPagingSourceFactory(): PagingSource<Int, EpisodeDatabaseEntity> {
        val name = EpisodesSearchParams.name
        val episode = EpisodesSearchParams.episode
        val airDate = EpisodesSearchParams.airDate
        val responseDatabase: PagingSource<Int, EpisodeDatabaseEntity>
        if (name != "" || episode != "" || airDate != "") {
            responseDatabase = database.episodeDao().getByFilter(name, episode, airDate)
            checkQueryForEmpty(name, episode, airDate)
        } else {
            responseDatabase = database.episodeDao().getAll()
        }
        return responseDatabase
    }

    private fun checkQueryForEmpty(
        name: String,
        episode: String,
        airDate: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val check =
                database.episodeDao().getByFilterForCheck(name, episode, airDate)
            if (check.isEmpty()) {
                StateWarnings.emptyDataWarning = true
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}