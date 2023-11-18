package com.marzec.cheatday.di

import android.content.Context
import com.google.gson.Gson
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
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

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
    }
}
