package com.marzec.cheatday.di

import androidx.lifecycle.ViewModelProvider
import com.marzec.cheatday.viewmodel.CustomViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: CustomViewModelFactory): ViewModelProvider.Factory
}
