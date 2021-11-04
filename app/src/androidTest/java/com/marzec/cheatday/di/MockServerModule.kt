package com.marzec.cheatday.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import okhttp3.mockwebserver.MockWebServer

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiUrlsModule::class]
)
class MockServerModule {

    @Provides
    @Singleton
    fun provideServer() = MockWebServer()

    @Provides
    @LoginApiUrl
    fun provideLoginApiUrl(server: MockWebServer) = server.url("/").toString()

    @Provides
    @CheatDayApiUrl
    fun provideCheatDayApiUrl(server: MockWebServer) = server.url("/").toString()
}
