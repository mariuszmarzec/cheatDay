package com.marzec.cheatday.di

import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.interactor.DaysInteractorImpl
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.interactor.WeightInteractorImpl
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
interface InteractorModule {

    @Binds
    fun bindDaysInteractor(interactor: DaysInteractorImpl): DaysInteractor

    @ExperimentalCoroutinesApi
    @Binds
    fun bindWeightInteractor(interactor: WeightInteractorImpl): WeightInteractor
}