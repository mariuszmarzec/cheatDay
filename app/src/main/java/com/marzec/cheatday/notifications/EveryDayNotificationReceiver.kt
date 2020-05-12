package com.marzec.cheatday.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import dagger.android.AndroidInjection
import javax.inject.Inject

class EveryDayNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        notificationHelper.show(
            context.getString(R.string.app_name),
            context.getString(R.string.notification_every_day_sum_up),
            Constants.NOTIFICATION_ID_NOTIFICATION_EVERY_DAY
        )
    }
}