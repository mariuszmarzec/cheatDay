package com.marzec.cheatday.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.marzec.cheatday.notifications.NotificationHelper
import dagger.android.AndroidInjection
import javax.inject.Inject

class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        Log.d("KURWA", "KURWA")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            notificationHelper.scheduleEveryDayNotification()
        }
    }
}