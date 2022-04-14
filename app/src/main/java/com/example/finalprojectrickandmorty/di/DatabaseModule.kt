package com.example.finalprojectrickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDao
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabase
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDao
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabase
import com.example.finalprojectrickandmorty.database.locationDB.LocationDao
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCharacterDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            CharacterDatabase.CHARACTER_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCharacterDao(characterDatabase: CharacterDatabase): CharacterDao {
        return characterDatabase.characterDao()
    }

    @Singleton
    @Provides
    fun provideEpisodeDatabase(@ApplicationContext context: Context): EpisodeDatabase {
        return Room.databaseBuilder(
            context,
            EpisodeDatabase::class.java,
            EpisodeDatabase.EPISODE_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideEpisodeDao(episodeDatabase: EpisodeDatabase): EpisodeDao {
        return episodeDatabase.episodeDao()
    }

    @Singleton
    @Provides
    fun provideLocationDatabase(@ApplicationContext context: Context): LocationDatabase {
        return Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            LocationDatabase.LOCATION_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideLocationDao(locationDatabase: LocationDatabase): LocationDao {
        return locationDatabase.locationDao()
    }

}