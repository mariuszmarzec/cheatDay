package com.marzec.cheatday.di

import android.util.Log
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

    private val responses = mutableMapOf<Pair<Method, String>, MockResponse>()

    fun setResponse(method: Method, path: String, response: MockResponse) {
        responses[method to path] = response
    }

    override fun dispatch(request: RecordedRequest): MockResponse =
        responses[Method.valueOf(request.method.orEmpty().uppercase()) to request.path]
            ?: throw IllegalArgumentException("No response for ${request.path} in mock server").also {
                Log.e(this@MockWebDispatcher::class.simpleName, it.message.orEmpty(), it)
            }

    fun clear() {
        responses.clear()
    }
}

enum class Method(val value: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE")
}
