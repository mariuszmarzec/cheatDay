package com.marzec.cheatday.di

import com.marzec.cheatday.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
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
    @ApiHost
    fun provideApiHost(): String = Api.HOST

    @Provides
    @LoginApiUrl
    fun provideLoginApiUrl(server: MockWebServer) = server.url("/").toString()

    @Provides
    @CheatDayApiUrl
    fun provideCheatDayApiUrl(server: MockWebServer) = server.url("/").toString()
}

object MockWebDispatcher : Dispatcher() {

    private val responses = mutableListOf<Pair<String, MockResponse>>()

    fun setResponse(path: String, response: MockResponse) {
        responses.add(path to response)
    }

    override fun dispatch(request: RecordedRequest): MockResponse {
        val pair = responses.first { request.path.orEmpty() == it.first }
        responses.remove(pair)
        return pair.second
    }

    fun clear() {
        responses.clear()
    }
}
