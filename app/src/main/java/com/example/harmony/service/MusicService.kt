package com.example.harmony.service

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
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].title)
            .setContentTitle(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.harmony_splash_screen)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.harmony_splash_screen))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_icon, "Previous", null)
            .addAction(R.drawable.pause_icon, "Pause", null)
            .addAction(R.drawable.next_icon, "Next", null)
            .addAction(R.drawable.exit_icon, "Exit", null)
            .build()

        startForeground(10, notification)
    }
}