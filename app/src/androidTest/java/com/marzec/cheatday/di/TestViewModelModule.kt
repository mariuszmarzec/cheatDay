package com.marzec.cheatday.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nhaarman.mockitokotlin2.mock
import javax.inject.Provider

object TestViewModelModule : ViewModelModule() {

    val viewModelFactory = mock<ViewModelProvider.Factory>()

    override fun bindViewModelFactory(creators: Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory {
        return viewModelFactory
    }
}