package com.marzec.cheatday.di

import androidx.lifecycle.ViewModel
import com.marzec.cheatday.feature.home.addnewresult.AddNewWeightResultViewModel
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterViewModel
import com.marzec.cheatday.feature.home.weights.WeightsViewModel
import com.marzec.cheatday.viewmodel.AssistedSavedStateViewModelFactory
import com.marzec.cheatday.viewmodel.InjectingSavedStateViewModelFactory
import com.marzec.cheatday.viewmodel.ViewModelFactory
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import javax.inject.Provider

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
    fun bindVMFactory(f: AddNewWeightResultViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}


@Module
open class ViewModelFactoryModule {

    @Provides
    @Reusable
    open fun provideViewModelFactory(
        assistedFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards AssistedSavedStateViewModelFactory<out ViewModel>>,
        viewModelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory = InjectingSavedStateViewModelFactory(assistedFactories, viewModelProviders)
}