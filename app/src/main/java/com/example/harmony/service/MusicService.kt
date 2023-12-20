package com.example.harmony.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.harmony.R
import com.example.harmony.ui.PlayerActivity

class MusicService: Service() {
    private var myBinder = MyBinder()
    var mediaPlayer:MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "Harmony")
        return myBinder
    }

    inner class MyBinder: Binder(){
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(){

        val previousIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val previousPendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            previousIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PAUSE)
        val pausePendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            exitIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].title)
            .setContentTitle(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.harmony_splash_screen)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.harmony_splash_screen))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_notification, "Previous", previousPendingIntent)
            .addAction(R.drawable.pause_notification, "Pause", pausePendingIntent)
            .addAction(R.drawable.next_notification, "Next", nextPendingIntent)
            .addAction(R.drawable.exit_notification, "Exit", exitPendingIntent)
            .build()

        startForeground(10, notification)
    }
}