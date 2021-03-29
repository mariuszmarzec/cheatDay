package com.marzec.cheatday.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.marzec.cheatday.common.CurrentUserProtoSerializer
import com.marzec.cheatday.App
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.screen.weights.model.WeightsMapper
import com.marzec.cheatday.screen.weights.model.WeightsMapperImpl
import com.marzec.cheatday.model.domain.CurrentUserProto
import com.marzec.cheatday.notifications.NotificationHelper
import com.marzec.cheatday.notifications.NotificationHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
interface AppModule {

    @Binds
    fun bindContext(application: App): Context

    @Binds
    @Singleton
    fun bindNotificationHelper(notificationHelper: NotificationHelperImpl): NotificationHelper

    @Binds
    fun bindWeightsMapper(mapper: WeightsMapperImpl): WeightsMapper

    companion object {

        @Provides
        fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Singleton
        fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)

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
        fun provideDataStore(context: Context): DataStore<Preferences> =
            context.createDataStore(name = "user_preferences")

        @Provides
        @Singleton
        fun provideCurrentUserDataStore(context: Context): DataStore<CurrentUserProto> =
            context.createDataStore(
                fileName = "CurrentUserProto.pb",
                serializer = CurrentUserProtoSerializer
            )
    }
}

