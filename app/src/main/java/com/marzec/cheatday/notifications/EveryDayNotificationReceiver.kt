package com.marzec.cheatday.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.repository.UserPreferencesRepository
import dagger.android.AndroidInjection
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class EveryDayNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var daysInteractor: DaysInteractor

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
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