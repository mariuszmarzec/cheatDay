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


@Module
interface AppModule {
    @Binds
    fun bindContext(application: App): Context

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
        fun provideSharedPreferences(context: Context): RxSharedPreferences {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return RxSharedPreferences.create(preferences)
        }
    }
}