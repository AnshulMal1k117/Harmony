package com.example.harmony.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.harmony.R
import com.example.harmony.data.setSongPosition
import com.example.harmony.ui.PlayerActivity
import kotlin.system.exitProcess

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ApplicationClass.PREVIOUS -> songChange(increase = false, context = context!!)
            ApplicationClass.PAUSE -> if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT -> songChange(increase = true, context = context!!)
            ApplicationClass.EXIT -> {
                PlayerActivity.musicService!!.stopForeground(true)
                PlayerActivity.musicService = null
                exitProcess(1)
            }
        }
    }

    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_notification)
        PlayerActivity.binding.playPauseButtonPlayerActivity.setIconResource(R.drawable.pause_icon)
    }

    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_notification)
        PlayerActivity.binding.playPauseButtonPlayerActivity.setIconResource(R.drawable.play_icon)
    }

    private fun songChange(increase: Boolean, context: Context){
        setSongPosition(increase = increase)
        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.harmony_logo_splash_screen).centerCrop())
            .into(PlayerActivity.binding.songImagePlayerActivity)
        PlayerActivity.binding.songNamePlayerActivity.text = PlayerActivity.musicListPlayerActivity[PlayerActivity.songPosition].title
        playMusic()
    }
}