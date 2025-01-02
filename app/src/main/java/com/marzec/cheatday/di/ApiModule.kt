package com.marzec.cheatday.di

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.FeatureToggleApi
import com.marzec.cheatday.api.LocalLoginApi
import com.marzec.cheatday.api.LoginApi
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Duration
import javax.inject.Named
import javax.inject.Provider
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
    @LoginApiRetrofit
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
    @FeatureToggleApiRetrofit
    fun provideFeatureToggleApiRetrofit(
        httpClient: OkHttpClient,
        @FeatureToggleApiUrl apiUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @CheatDayApiRetrofit
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
    @Named(LOGIN_API_REMOTE_CLIENT)
    fun provideRemoteLoginApi(@LoginApiRetrofit retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)

    @Provides
    @Singleton
    @Named(LOGIN_API_LOCAL_CLIENT)
    fun provideLocalLoginApi(): LoginApi = LocalLoginApi()

    @Provides
    @Singleton
    fun provideLoginApi(
        @ApiHost apiHost: String,
        @Named(LOGIN_API_REMOTE_CLIENT) remoteLoginApi: Provider<LoginApi>,
        @Named(LOGIN_API_LOCAL_CLIENT) localLoginApi: Provider<LoginApi>
    ): LoginApi = if (apiHost == Api.LOCALHOST_API) {
        localLoginApi.get()
    } else {
        remoteLoginApi.get()
    }

    @Provides
    @Singleton
    fun provideFeatureToggleApi(@FeatureToggleApiRetrofit retrofit: Retrofit): FeatureToggleApi = retrofit.create(
        FeatureToggleApi::class.java
    )

    @Provides
    @Singleton
    fun provideWeightApi(@CheatDayApiRetrofit retrofit: Retrofit): WeightApi = retrofit.create(
        WeightApi::class.java
    )

    private companion object {
        const val TIMEOUT: Long = 10_000
        const val HTTP_UNAUTHORIZED_STATUS = 401

        const val LOGIN_API_REMOTE_CLIENT = "LOGIN_API_REMOTE_CLIENT"
        const val LOGIN_API_LOCAL_CLIENT = "LOGIN_API_LOCAL_CLIENT"
    }
}

@Qualifier
annotation class LoginApiRetrofit

@Qualifier
annotation class FeatureToggleApiRetrofit

@Qualifier
annotation class CheatDayApiRetrofit

@Qualifier
annotation class ApiHost

@Qualifier
annotation class LoginApiUrl

@Qualifier
annotation class FeatureToggleApiUrl

@Qualifier
annotation class CheatDayApiUrl
