package com.marzec.cheatday.feature.home.addnewresult

import androidx.lifecycle.ViewModel
import com.marzec.cheatday.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AddNewWeightResultModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddNewWeightResultViewModel::class)
    fun bindDaysCounter(viewModel: AddNewWeightResultViewModel): ViewModel
}