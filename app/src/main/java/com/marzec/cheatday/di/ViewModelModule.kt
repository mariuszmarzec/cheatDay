package com.marzec.cheatday.di

import androidx.lifecycle.ViewModel
import com.marzec.cheatday.feature.home.MainViewModel
import com.marzec.cheatday.feature.home.addnewresult.AddNewWeightResultViewModel
import com.marzec.cheatday.feature.home.chart.ChartsViewModel
import com.marzec.cheatday.feature.home.dayscounter.DaysCounterViewModel
import com.marzec.cheatday.feature.home.login.model.LoginViewModel
import com.marzec.cheatday.feature.home.weights.WeightsViewModel
import com.marzec.cheatday.viewmodel.AssistedSavedStateViewModelFactory
import com.marzec.cheatday.viewmodel.InjectingSavedStateViewModelFactory
import com.marzec.cheatday.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Suppress("unused")
@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMain(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLogin(viewModel: LoginViewModel): ViewModel

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
    @ViewModelKey(ChartsViewModel::class)
    fun bindChartsViewModel(viewModel: ChartsViewModel): ViewModel

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