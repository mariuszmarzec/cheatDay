package com.marzec.cheatday.di

import com.marzec.cheatday.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApiUrlsModule {
    @Provides
    @LoginApiUrl
    fun provideLoginApiUrl() = Api.BASE_URL

    @Provides
    @CheatDayApiUrl
    fun provideCheatDayApiUrl() = Api.BASE_CHEAT_URL
}
