package com.example.nutriapp.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.nutriapp.R
import com.example.nutriapp.notification.AlarmScheduler
import com.example.nutriapp.notification.NotificationPreferences

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 1. Muestra la notificación
        showNotification(context)

        // 2. Vuelve a programar la alarma para el día siguiente
        rescheduleAlarm(context)
    }

    private fun showNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "meal_reminder_channel")
            .setSmallIcon(R.drawable.ic_notification_bell)
            .setContentTitle("¡Hora de registrar tu comida!")
            .setContentText("No te olvides de registrar tus alimentos para llevar un seguimiento de tu progreso.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun rescheduleAlarm(context: Context) {
        val prefs = NotificationPreferences(context)
        if (prefs.areNotificationsEnabled()) {
            val scheduler = AlarmScheduler(context)
            scheduler.schedule(prefs.getHour(), prefs.getMinute())
        }
    }
}
