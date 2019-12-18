package com.marzec.cheatday

import com.marzec.cheatday.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import net.danlew.android.joda.JodaTimeAndroid

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}