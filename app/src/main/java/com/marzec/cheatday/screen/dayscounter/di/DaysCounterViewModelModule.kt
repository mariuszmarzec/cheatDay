package com.marzec.cheatday.screen.dayscounter.di

import com.marzec.cheatday.screen.dayscounter.model.DaysCounterState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DaysCounterViewModelModule {

    @Provides
    fun provideState(): DaysCounterState = DaysCounterState.DEFAULT_STATE
}
