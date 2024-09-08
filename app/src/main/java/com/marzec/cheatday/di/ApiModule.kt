package com.marzec.cheatday.di

import android.util.Log
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.LoginApi
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Duration
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideHttpClient(userRepository: UserRepository): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(Duration.ofMillis(TIMEOUT))
            .writeTimeout(Duration.ofMillis(TIMEOUT))
            .readTimeout(Duration.ofMillis(TIMEOUT))
            .addNetworkInterceptor(Interceptor { chain ->
                var request = chain.request()
                Log.d("REQUEST", "${request.url.toString()}")

                runBlocking { userRepository.getCurrentUserWithAuthToken() }?.let { user ->
                    request = request.newBuilder()
                        .addHeader(Api.Headers.AUTHORIZATION, user.auth)
                        .build()
                }

                val response = chain.proceed(request)
                if (response.code == HTTP_UNAUTHORIZED_STATUS) {
                    runBlocking { userRepository.clearCurrentUser() }
                }
                response
            })
            .build()
    }


    @Provides
    @Singleton
    @LoginApiClient
    fun provideRetrofitForLoginApi(
        httpClient: OkHttpClient,
        @LoginApiUrl apiUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @CheatDayApiClient
    fun provideRetrofitForCheatDayApi(
        httpClient: OkHttpClient,
        @CheatDayApiUrl apiUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideLoginApi(@LoginApiClient retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)

    @Provides
    @Singleton
    fun provideWeightApi(@CheatDayApiClient retrofit: Retrofit): WeightApi = retrofit.create(
        WeightApi::class.java
    )

    private companion object {
        const val TIMEOUT: Long = 10_000
        const val HTTP_UNAUTHORIZED_STATUS = 401
    }
}

@Qualifier
annotation class LoginApiClient

@Qualifier
annotation class CheatDayApiClient

@Qualifier
annotation class ApiHost

@Qualifier
annotation class LoginApiUrl

@Qualifier
annotation class CheatDayApiUrl
