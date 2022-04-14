package com.example.finalprojectrickandmorty.di

import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabase
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabase
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabase
import com.example.finalprojectrickandmorty.network.characterNetwork.CharacterNetwork
import com.example.finalprojectrickandmorty.network.episodeNetwork.EpisodeNetwork
import com.example.finalprojectrickandmorty.network.locationNetwork.LocationNetwork
import com.example.finalprojectrickandmorty.repository.characterRepository.CharacterRepository
import com.example.finalprojectrickandmorty.repository.episodeRepository.EpisodeRepository
import com.example.finalprojectrickandmorty.repository.locationRepository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCharacterRepository(
        characterNetwork: CharacterNetwork,
        database: CharacterDatabase,
    ): CharacterRepository {
        return CharacterRepository(characterNetwork, database)
    }

    @Singleton
    @Provides
    fun provideEpisodeRepository(
        episodeNetwork: EpisodeNetwork,
        database: EpisodeDatabase,
    ): EpisodeRepository {
        return EpisodeRepository(episodeNetwork, database)
    }

    @Singleton
    @Provides
    fun provideLocationRepository(
        locationNetwork: LocationNetwork,
        database: LocationDatabase,
    ): LocationRepository {
        return LocationRepository(locationNetwork, database)
    }

}