package com.example.finalprojectrickandmorty.repository.characterRepository

import androidx.paging.*
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabase
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.network.characterNetwork.CharacterNetwork
import com.example.finalprojectrickandmorty.util.CharactersSearchParams
import com.example.finalprojectrickandmorty.util.StateWarnings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class CharacterRepository @Inject constructor(
    private val characterNetwork: CharacterNetwork,
    private val database: CharacterDatabase,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersFromMediator(): Flow<PagingData<CharacterDatabaseEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            remoteMediator = CharacterRemoteMediator(
                characterNetwork,
                database
            ),
            pagingSourceFactory = {
                createPagingSourceFactory()
            }
        ).flow
    }

    private fun createPagingSourceFactory(): PagingSource<Int, CharacterDatabaseEntity> {
        val name = CharactersSearchParams.name
        val status = CharactersSearchParams.status
        val species = CharactersSearchParams.species
        val type = CharactersSearchParams.type
        val gender = CharactersSearchParams.gender
        val responseDatabase: PagingSource<Int, CharacterDatabaseEntity>
        if (name != "" || status != "" || species != "" || type != "" || gender != "") {
            responseDatabase =
                database.characterDao().getByFilter(name, status, species, type, gender)
            checkQueryForEmpty(name, status, species, type, gender)
        } else {
            responseDatabase = database.characterDao().getAll()
        }
        return responseDatabase
    }

    private fun checkQueryForEmpty(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val check =
                database.characterDao().getByFilterForCheck(name, status, species, type, gender)
            if (check.isEmpty()) {
                StateWarnings.emptyDataWarning = true
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}