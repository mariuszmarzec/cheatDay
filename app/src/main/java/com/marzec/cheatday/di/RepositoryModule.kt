package com.marzec.cheatday.di

import com.marzec.cheatday.repository.*
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindDayRepository(repository: DayRepositoryImpl): DayRepository

    @Binds
    fun bindTargetWeightRepository(repository: TargetWeightRepositoryImpl): TargetWeightRepository

    @Binds
    fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    fun bindWeightResultRepository(repository: WeightResultRepositoryImpl): WeightResultRepository
}
