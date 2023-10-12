package com.marzec.cheatday.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.marzec.cheatday.common.CurrentUserProtoSerializer
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.model.domain.CurrentUserProto
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


private val Context.userPreferencesStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

private val Context.userDataStore: DataStore<CurrentUserProto> by dataStore(
    "user_preferences",
    CurrentUserProtoSerializer
)

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindNotificationHelper(notificationHelper: NotificationHelperImpl): NotificationHelper

    companion object {

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

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.userPreferencesStore

        @Provides
        @Singleton
        fun provideCurrentUserDataStore(@ApplicationContext context: Context): DataStore<CurrentUserProto> =
            context.userDataStore
    }
}

