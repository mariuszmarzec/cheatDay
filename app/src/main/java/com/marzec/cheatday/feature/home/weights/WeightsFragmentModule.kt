package com.marzec.cheatday.feature.home.weights

import androidx.lifecycle.ViewModel
import com.marzec.cheatday.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WeightsFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(WeightsViewModel::class)
    fun bindDaysCounter(viewModel: WeightsViewModel): ViewModel
}