package com.marzec.cheatday.screen.addnewresult.di

import com.marzec.cheatday.screen.addnewresult.model.AddWeightState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AddNewWeightResultViewModelModule {

    @Provides
    fun provideDefaultState(): AddWeightState = AddWeightState.INITIAL
}
