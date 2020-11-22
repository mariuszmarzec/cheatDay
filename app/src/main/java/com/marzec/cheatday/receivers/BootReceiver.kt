package com.marzec.cheatday.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.marzec.cheatday.notifications.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            notificationHelper.scheduleEveryDayNotification()
        }
    }
}