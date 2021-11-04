package com.marzec.cheatday.screen.home.di

import com.marzec.cheatday.screen.home.model.MainState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class MainModel {

    @Provides
    fun provideMainState(): MainState = MainState.INITIAL
}
