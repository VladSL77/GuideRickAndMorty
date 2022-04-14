package com.example.finalprojectrickandmorty.viewModel.locationViewModel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.repository.locationRepository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(
    locationRepository: LocationRepository,
) : ViewModel() {

    val dataSource: Flow<PagingData<LocationDatabaseEntity>> =
        locationRepository.getLocationFromMediator()

}