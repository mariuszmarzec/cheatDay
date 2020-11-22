package com.marzec.cheatday.di

import androidx.hilt.lifecycle.ViewModelAssistedFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marzec.cheatday.feature.home.weights.WeightsViewModel
import com.marzec.cheatday.feature.home.weights.WeightsViewModel_AssistedFactory
import com.nhaarman.mockitokotlin2.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
@InstallIn(ApplicationComponent::class)
object TestViewModelFactoryModule {

    val viewModelFactory = mock<ViewModelProvider.Factory>()

    fun createFactoryMock(): ViewModelAssistedFactory<out ViewModel> {
        return mock<ViewModelFactory>().apply {
            whenever(create(any(), anyOrNull())) doReturn viewModelFactory
        }
    }

}

@InstallIn(ActivityRetainedComponent::class)
@Module
interface AssistedFactoryModule{
    @Binds
    @IntoMap
    @StringKey("com.marzec.cheatday.feature.home.weights.WeightsViewModel")
    fun bind(factory: WeightsViewModel_AssistedFactory): ViewModelAssistedFactory<out ViewModel>
}