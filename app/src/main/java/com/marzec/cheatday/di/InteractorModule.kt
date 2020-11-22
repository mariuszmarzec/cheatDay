package com.marzec.cheatday.di

import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.interactor.DaysInteractorImpl
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.interactor.WeightInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(ApplicationComponent::class)
interface InteractorModule {

    @Binds
    fun bindDaysInteractor(interactor: DaysInteractorImpl): DaysInteractor

    @ExperimentalCoroutinesApi
    @Binds
    fun bindWeightInteractor(interactor: WeightInteractorImpl): WeightInteractor
}