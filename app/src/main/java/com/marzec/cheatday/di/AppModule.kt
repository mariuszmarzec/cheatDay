package com.marzec.cheatday.di

import android.content.Context
import dagger.Binds
import dagger.Module
import com.marzec.cheatday.App
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.db.dao.UserDao
import dagger.Provides
import javax.inject.Singleton

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
    }
}