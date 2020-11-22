package com.marzec.cheatday.di

import android.content.Context
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.feature.home.weights.WeightsMapper
import com.marzec.cheatday.feature.home.weights.WeightsMapperImpl
import com.marzec.cheatday.notifications.NotificationHelper
import com.marzec.cheatday.notifications.NotificationHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindNotificationHelper(notificationHelper: NotificationHelperImpl): NotificationHelper

    @Binds
    fun bindWeightsMapper(mapper: WeightsMapperImpl): WeightsMapper

    companion object {

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getInstance(context)

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
        fun provideSharedPreferences(@ApplicationContext context: Context): RxSharedPreferences {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return RxSharedPreferences.create(preferences)
        }
    }
}