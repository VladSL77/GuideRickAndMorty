package com.example.finalprojectrickandmorty.viewModel.locationViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.repository.locationRepository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OriginViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
) : ViewModel(){

    /* Оставлю на будущее
    private val mutableSingleLocation = MutableLiveData<LocationDatabaseEntity>()
    val singleLocation : LiveData<LocationDatabaseEntity>
        get() = mutableSingleLocation
    suspend fun singleLocation() {
        mutableSingleLocation.value = locationRepository.getSingleLocation()
    }
     */

}