package com.marzec.cheatday

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.common.Constants.ALARM_ID_NOTIFICATION_EVERY_DAY
import com.marzec.cheatday.di.DaggerAppComponent
import com.marzec.cheatday.notifications.EveryDayNotificationReceiver
import com.marzec.cheatday.notifications.NotificationHelper
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import javax.inject.Inject

class App : DaggerApplication() {

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