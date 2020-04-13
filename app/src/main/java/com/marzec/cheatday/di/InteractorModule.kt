package com.marzec.cheatday.di

import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.interactor.DaysInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface InteractorModule {

    @Binds
    fun bindDaysInteractor(interactor: DaysInteractorImpl): DaysInteractor
}