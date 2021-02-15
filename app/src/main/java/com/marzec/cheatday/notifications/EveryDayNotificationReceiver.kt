package com.marzec.cheatday.notifications

import android.content.Context
import android.content.Intent
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.receivers.HiltBroadcastReceiver
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

class EveryDayNotificationReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var daysInteractor: DaysInteractor

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        runBlocking {
            if (!daysInteractor.isStateSettled()) {
                notificationHelper.show(
                    context.getString(R.string.app_name),
                    context.getString(R.string.notification_every_day_sum_up),
                    Constants.NOTIFICATION_ID_NOTIFICATION_EVERY_DAY
                )
            }
        }
    }
}