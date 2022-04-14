package com.example.finalprojectrickandmorty.viewModel.episodeViewModel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.repository.episodeRepository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    episodeRepository: EpisodeRepository,
) : ViewModel() {

    val dataSource: Flow<PagingData<EpisodeDatabaseEntity>> =
        episodeRepository.getEpisodeFromMediator()

}