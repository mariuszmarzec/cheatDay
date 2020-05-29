package com.marzec.cheatday.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marzec.cheatday.viewmodel.CustomViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Suppress("unused")
@Module
open class ViewModelModule {

    @Provides
    open fun bindViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory = CustomViewModelFactory(creators)
}
