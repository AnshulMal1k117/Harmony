package com.example.harmony.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.harmony.MainActivity
import com.example.harmony.R
import com.example.harmony.data.getImageArt
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

    fun showNotification(playPauseButton: Int){
        val intent = Intent(baseContext, MainActivity::class.java)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }


        val previousIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val previousPendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            previousIntent, flag)

        val pauseIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PAUSE)
        val pausePendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            pauseIntent, flag)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            nextIntent, flag)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0,
            exitIntent, flag)

        val imageArt = getImageArt(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].path)
        val image = if (imageArt != null){
            BitmapFactory.decodeByteArray(imageArt, 0, imageArt.size)
        }else{
            BitmapFactory.decodeResource(resources, R.drawable.harmony_splash_screen)
        }

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].title)
            .setContentTitle(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.harmony_splash_screen)
            .setLargeIcon(image)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_notification, "Previous", previousPendingIntent)
            .addAction(playPauseButton, "Pause", pausePendingIntent)
            .addAction(R.drawable.next_notification, "Next", nextPendingIntent)
            .addAction(R.drawable.exit_notification, "Exit", exitPendingIntent)
            .build()

        startForeground(10, notification)
    }
}