package com.marzec.cheatday.di

import com.marzec.cheatday.screen.home.MainActivity
import com.marzec.cheatday.screen.home.di.MainActivityModule
import com.marzec.cheatday.notifications.EveryDayNotificationReceiver
import com.marzec.cheatday.receivers.BootReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @BroadcastScope
    @ContributesAndroidInjector
    abstract fun bindEveryDayNotificationReceiver(): EveryDayNotificationReceiver

    @BroadcastScope
    @ContributesAndroidInjector
    abstract fun bindBootReceiver(): BootReceiver
}