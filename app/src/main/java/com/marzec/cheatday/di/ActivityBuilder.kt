package com.marzec.cheatday.di

import com.marzec.cheatday.feature.home.MainActivity
import com.marzec.cheatday.feature.home.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

}