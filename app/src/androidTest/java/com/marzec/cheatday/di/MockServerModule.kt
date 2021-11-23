package com.marzec.cheatday.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import okhttp3.mockwebserver.RecordedRequest

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

object MockWebDispatcher : Dispatcher() {

    private val map = hashMapOf<String, MockResponse>()

    fun setResponse(path: String, response: MockResponse) {
        map[path] = response
    }

    override fun dispatch(request: RecordedRequest): MockResponse =
        map.getValue(request.path.orEmpty())
}
