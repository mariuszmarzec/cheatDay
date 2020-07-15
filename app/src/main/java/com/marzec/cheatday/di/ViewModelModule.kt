package com.marzec.cheatday.di

import androidx.lifecycle.ViewModel
import com.marzec.cheatday.feature.home.addnewresult.AddNewWeightResultViewModel
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterViewModel
import com.marzec.cheatday.feature.home.weights.WeightsViewModel
import com.marzec.cheatday.viewmodel.AssistedSavedStateViewModelFactory
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@AssistedModule
@Module(includes = [AssistedInject_ViewModelModule::class])
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DaysCounterViewModel::class)
    fun bindDaysCounter(viewModel: DaysCounterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeightsViewModel::class)
    fun bindWeightsViewModel(viewModel: WeightsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddNewWeightResultViewModel::class)
    abstract fun bindVMFactory(f: AddNewWeightResultViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
