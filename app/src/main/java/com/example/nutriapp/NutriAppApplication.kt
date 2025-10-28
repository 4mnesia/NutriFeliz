package com.example.nutriapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NutriAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Recordatorio de Comidas"
            val descriptionText = "Canal para los recordatorios de registro de comidas."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("meal_reminder_channel", name, importance).apply {
                description = descriptionText
            }
            
            // Registrar el canal con el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
