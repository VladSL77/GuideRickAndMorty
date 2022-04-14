package com.example.finalprojectrickandmorty.viewModel.characterViewModel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.repository.characterRepository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    characterRepository: CharacterRepository,
) : ViewModel() {

    val dataSource: Flow<PagingData<CharacterDatabaseEntity>> =
        characterRepository.getCharactersFromMediator()

}