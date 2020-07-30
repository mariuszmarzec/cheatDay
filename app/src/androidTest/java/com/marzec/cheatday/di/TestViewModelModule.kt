package com.marzec.cheatday.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marzec.cheatday.viewmodel.AssistedSavedStateViewModelFactory
import com.marzec.cheatday.viewmodel.ViewModelFactory
import com.nhaarman.mockitokotlin2.*
import javax.inject.Provider

object TestViewModelFactoryModule : ViewModelFactoryModule() {

    val viewModelFactory = mock<ViewModelProvider.Factory>()

    override fun provideViewModelFactory(
        assistedFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards AssistedSavedStateViewModelFactory<out ViewModel>>,
        viewModelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory {
        return createFactoryMock()
    }

    fun createFactoryMock(): ViewModelFactory {
        return mock<ViewModelFactory>().apply {
            whenever(create(any(), anyOrNull())) doReturn viewModelFactory
        }
    }

}