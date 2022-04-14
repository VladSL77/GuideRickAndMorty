package com.example.finalprojectrickandmorty.di

import com.example.finalprojectrickandmorty.network.characterNetwork.CharacterNetwork
import com.example.finalprojectrickandmorty.network.episodeNetwork.EpisodeNetwork
import com.example.finalprojectrickandmorty.network.locationNetwork.LocationNetwork
import com.example.finalprojectrickandmorty.util.Constants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideCharacterService(retrofit: Retrofit.Builder): CharacterNetwork {
        return retrofit
            .build()
            .create(CharacterNetwork::class.java)
    }

    @Singleton
    @Provides
    fun provideEpisodeService(retrofit: Retrofit.Builder): EpisodeNetwork {
        return retrofit
            .build()
            .create(EpisodeNetwork::class.java)
    }

    @Singleton
    @Provides
    fun provideLocationService(retrofit: Retrofit.Builder): LocationNetwork {
        return retrofit
            .build()
            .create(LocationNetwork::class.java)
    }

}