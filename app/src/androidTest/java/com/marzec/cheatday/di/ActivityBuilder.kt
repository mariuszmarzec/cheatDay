package com.marzec.cheatday.di

import com.marzec.cheatday.DaggerTestActivity
import com.marzec.cheatday.DaggerTestActivityModule
import com.marzec.cheatday.feature.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [DaggerTestActivityModule::class])
    abstract fun bindDaggerTestActivity(): DaggerTestActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity
}