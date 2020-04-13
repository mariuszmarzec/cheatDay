package com.marzec.cheatday.feature.home.dayscounter

import androidx.lifecycle.ViewModel
import com.marzec.cheatday.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DaysCounterFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(DaysCounterViewModel::class)
    fun bindDaysCounter(viewModel: DaysCounterViewModel): ViewModel
}