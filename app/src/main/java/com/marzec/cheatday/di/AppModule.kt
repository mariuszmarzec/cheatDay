package com.marzec.cheatday.di

import android.content.Context
import dagger.Binds
import dagger.Module
import com.marzec.cheatday.App

@Module
interface AppModule {
    @Binds
    fun bindContext(application: App): Context
}