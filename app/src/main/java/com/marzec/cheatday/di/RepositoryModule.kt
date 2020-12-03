package com.marzec.cheatday.di

import com.marzec.cheatday.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface RepositoryModule {

    @Binds
    fun bindDayRepository(repository: DayRepositoryImpl): DayRepository

    @Binds
    fun bindTargetWeightRepository(repository: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    fun bindWeightResultRepository(repository: WeightResultRepositoryImpl): WeightResultRepository
}