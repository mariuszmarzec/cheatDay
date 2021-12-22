package com.marzec.cheatday.notifications

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.content.getSystemService
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.screen.home.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.joda.time.DateTime

interface NotificationHelper {
    fun show(title: String, content: String, id: Int)
    fun scheduleEveryDayNotification()
    fun createNotificationChannel()
}

class NotificationHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationHelper {

    override fun show(title: String, content: String, id: Int) {
        val notification = Notification.Builder(context, Constants.CHANNEL_ID_COMMON)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_background
                )
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    FLAG_IMMUTABLE
                )
            ).build()
        context.getSystemService<NotificationManager>()?.notify(
            id,
            notification
        )
    }

    override fun createNotificationChannel() {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            Constants.CHANNEL_ID_COMMON,
            name,
            importance
        ).apply {
            description = descriptionText
        }
        context.getSystemService<NotificationManager>()
            ?.createNotificationChannel(channel)
    }

    override fun scheduleEveryDayNotification() {
        val alarmManager = context.getSystemService<AlarmManager>()
        val timeInMillis = DateTime.now()
            .withTimeAtStartOfDay()
            .withHourOfDay(ALARM_HOUR)
            .apply {
                if (this.isAfterNow) {
                    plusDays(1)
                }
            }
            .millis

        val intent = Intent(context, EveryDayNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.ALARM_ID_NOTIFICATION_EVERY_DAY,
            intent,
            FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager?.cancel(pendingIntent)

        alarmManager
            ?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
    }

    companion object {
        private const val ALARM_HOUR = 22
    }
}
