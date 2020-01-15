package com.marzec.cheatday.di

import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Binds
import dagger.Module
import com.marzec.cheatday.App
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.dao.UserDao
import dagger.Provides
import javax.inject.Singleton
import androidx.preference.PreferenceManager
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.repository.*


@Module
interface AppModule {
    @Binds
    fun bindContext(application: App): Context

    @Binds
    fun bindTargetWeightRepository(repository: TargetWeightRepositoryImpl): TargetWeightRepository

    @Binds
    fun bindWeightResultRepository(repository: WeightResultRepositoryImpl): WeightResultRepository

    @Binds
    fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Module
    companion object {

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
        fun provideSharedPreferences(context: Context): RxSharedPreferences {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return RxSharedPreferences.create(preferences)
        }
    }
}