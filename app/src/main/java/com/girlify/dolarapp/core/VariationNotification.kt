package com.girlify.dolarapp.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.girlify.dolarapp.MainActivity
import com.girlify.dolarapp.R

class VariationNotification (
    private val context : Context,
    private val title : String,
    private val msg : String,
    private val notificationId: Int = 1
) {
    private val channelId = "My_channel"
    private val channelName= "My channel name"
    private val notificationActionOpen = "Open"

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun showNotification(){
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_launcher_background, notificationActionOpen, pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}