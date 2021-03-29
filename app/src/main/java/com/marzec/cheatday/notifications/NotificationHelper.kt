package com.marzec.cheatday.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.content.getSystemService
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.screen.home.MainActivity
import org.joda.time.DateTime
import javax.inject.Inject

interface NotificationHelper {
    fun show(title: String, content: String, id: Int)
    fun scheduleEveryDayNotification()
    fun createNotificationChannel()
}

class NotificationHelperImpl @Inject constructor(
    private val context: Context
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
                    0
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
            .withHourOfDay(22)
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
            PendingIntent.FLAG_UPDATE_CURRENT
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
}
