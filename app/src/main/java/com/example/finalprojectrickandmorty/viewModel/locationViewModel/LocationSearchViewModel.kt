package com.example.finalprojectrickandmorty.viewModel.locationViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationSearchViewModel : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<String>()
    val selectedItem: LiveData<String>
        get() = mutableSelectedItem

    fun selectItem(item: String) {
        mutableSelectedItem.value = item
    }

}