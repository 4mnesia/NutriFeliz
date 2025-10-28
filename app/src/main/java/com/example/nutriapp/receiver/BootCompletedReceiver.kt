package com.example.nutriapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.nutriapp.notification.AlarmScheduler
import com.example.nutriapp.notification.NotificationPreferences

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = NotificationPreferences(context)
            if (prefs.areNotificationsEnabled()) {
                val scheduler = AlarmScheduler(context)
                val hour = prefs.getHour()
                val minute = prefs.getMinute()
                scheduler.schedule(hour, minute)
            }
        }
    }
}
