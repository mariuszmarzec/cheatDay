package com.marzec.cheatday.di

import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.CheatDayApi
import com.marzec.cheatday.api.LoginApi
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideHttpClient(userRepository: UserRepository): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(Interceptor { chain ->
            var request = chain.request()
            runBlocking { userRepository.getCurrentUserWithAuth() }?.let { user ->
                request = request.newBuilder()
                    .addHeader(Api.Headers.AUTHORIZATION, user.auth)
                    .build()
            }

            val response = chain.proceed(request)
            if (response.code == 401) {
                runBlocking { userRepository.clearCurrentUser() }
            }
            response
        })
        .build()


    @Provides
    @Singleton
    @LoginApiClient
    fun provideRetrofitForLoginApi(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(Api.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @CheatDayApiClient
    fun provideRetrofitForCheatDayApi(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(Api.BASE_CHEAT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideLoginApi(@LoginApiClient retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)

    @Provides
    @Singleton
    fun provideCheatDayApi(@CheatDayApiClient retrofit: Retrofit): CheatDayApi = retrofit.create(
        CheatDayApi::class.java
    )

    @Provides
    @Singleton
    fun provideWeightApi(@CheatDayApiClient retrofit: Retrofit): WeightApi = retrofit.create(
        WeightApi::class.java
    )
}

@Qualifier
annotation class LoginApiClient

@Qualifier
annotation class CheatDayApiClient