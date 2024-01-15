package com.example.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        // Utwórz kanał powiadomień (wymagane w przypadku Androida 8.0 Oreo i nowszych)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channelId = "my_channel_id"
        val channelName = "My Channel"
        val channelDescription = "Description of my channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun sendNotification(title: String, content: String) {
        val channelId = "my_channel_id"
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(androidx.core.R.drawable.notify_panel_notification_icon_bg) // Ikona powiadomienia
            .setContentTitle(title) // Tytuł powiadomienia
            .setContentText(content) // Treść powiadomienia
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priorytet

        val notification = notificationBuilder.build()

        // Wysyłka powiadomienia
        notificationManager.notify(1, notification)
    }
}
