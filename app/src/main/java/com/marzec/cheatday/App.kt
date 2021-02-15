package com.marzec.cheatday

import com.marzec.cheatday.di.DaggerAppComponent
import com.marzec.cheatday.notifications.NotificationHelper
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

open class App : DaggerApplication() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        notificationHelper.createNotificationChannel()
        notificationHelper.scheduleEveryDayNotification()
    }
}