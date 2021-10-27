package com.marzec.cheatday

import android.app.Application
import com.marzec.cheatday.notifications.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        notificationHelper.createNotificationChannel()
        notificationHelper.scheduleEveryDayNotification()
    }
}

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}