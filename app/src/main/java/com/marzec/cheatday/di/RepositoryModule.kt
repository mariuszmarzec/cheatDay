package com.marzec.cheatday.di

import com.marzec.cheatday.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindDayRepository(repository: DayRepositoryImpl): DayRepository

    @Binds
    fun bindTargetWeightRepository(repository: DataStoreUserPreferencesRepository): UserPreferencesRepository
}