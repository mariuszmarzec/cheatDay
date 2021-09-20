package com.marzec.cheatday.screen.login.di

import com.marzec.cheatday.screen.login.model.LoginData
import com.marzec.mvi.State
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class LoginViewModelModule {

    @Provides
    fun provideLoginState(): State<LoginData> = State.Data(LoginData.INITIAL)
}
