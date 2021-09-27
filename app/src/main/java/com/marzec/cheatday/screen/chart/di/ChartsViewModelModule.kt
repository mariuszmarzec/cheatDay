package com.marzec.cheatday.screen.chart.di

import com.marzec.cheatday.screen.chart.model.ChartsData
import com.marzec.mvi.State
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ChartsViewModelModule {

    @Provides
    fun provideLoginState(): State<ChartsData> = State.Loading(ChartsData.INITIAL)
}
