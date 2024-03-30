package com.marzec.cheatday.di

import android.content.Context
import com.google.gson.Gson
import com.marzec.cache.Cache
import com.marzec.cache.MemoryCache
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.notifications.NotificationHelper
import com.marzec.cheatday.notifications.NotificationHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindNotificationHelper(notificationHelper: NotificationHelperImpl): NotificationHelper

    companion object {

        @Provides
        @Singleton
        fun provideGson() = Gson()

        @Provides
        fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Named(UPDATER_COROUTINE_SCOPE)
        fun provideUpdaterCoroutineScope() = CoroutineScope(newSingleThreadContext("updater"))

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            AppDatabase.getInstance(context)

        @Provides
        @Singleton
        fun provideUserDao(database: AppDatabase): UserDao = database.getUserDao()

        @Provides
        @Singleton
        fun provideWeightDao(database: AppDatabase): WeightDao = database.getWeightDao()

        @Provides
        @Singleton
        fun provideDayDao(database: AppDatabase): DayDao = database.getDayDao()

        @Provides
        @Singleton
        fun provideMemoryCache() : Cache = MemoryCache()

        const val UPDATER_COROUTINE_SCOPE = "UPDATER_COROUTINE_SCOPE"
    }
}