package com.example.nutriapp.notification

import android.content.Context

class NotificationPreferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val KEY_HOUR = "notification_hour"
        const val KEY_MINUTE = "notification_minute"
    }

    fun areNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
    }

    fun getHour(): Int {
        return sharedPreferences.getInt(KEY_HOUR, 20) // Default 20:00
    }

    fun getMinute(): Int {
        return sharedPreferences.getInt(KEY_MINUTE, 0)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        sharedPreferences.edit()
            .putInt(KEY_HOUR, hour)
            .putInt(KEY_MINUTE, minute)
            .apply()
    }
}
