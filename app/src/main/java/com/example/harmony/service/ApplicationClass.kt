package com.example.harmony.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class ApplicationClass: Application() {

    companion object{
        const val CHANNEL_ID = "channel1"
        const val PAUSE = "pause"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
    }
    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(CHANNEL_ID, "Now Playing", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.description = "This channel shows the current song"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}