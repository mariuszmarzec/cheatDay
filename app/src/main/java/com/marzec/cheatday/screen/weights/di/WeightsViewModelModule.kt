package com.marzec.cheatday.screen.weights.di

import com.marzec.cheatday.screen.weights.model.WeightsData
import com.marzec.mvi.State
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class WeightsViewModelModule {

    @Provides
    fun provideDefaultState(): State<WeightsData> = State.Loading()
}
