package com.example.finalprojectrickandmorty.viewModel.characterViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CharacterSearchViewModel : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<String>()
    val selectedItem: LiveData<String>
        get() = mutableSelectedItem

    fun selectItem(item: String) {
        mutableSelectedItem.value = item
    }

}