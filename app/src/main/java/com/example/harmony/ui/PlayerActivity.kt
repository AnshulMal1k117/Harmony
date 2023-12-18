package com.example.harmony.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.harmony.MainActivity
import com.example.harmony.R
import com.example.harmony.data.Music
import com.example.harmony.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    companion object{
        lateinit var musicListPlayerActivity: ArrayList<Music>
        var songPosition:Int = 0
        var mediaPlayer: MediaPlayer? = null
        var isPlaying:Boolean = false
    }

    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_Harmony)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeLayout()
        binding.playPauseButtonPlayerActivity.setOnClickListener{
            if(isPlaying) pauseMusic()
            else playMusic()
        }
        binding.previousButtonPlayerActivity.setOnClickListener { songChange(clickNext = false) }
        binding.nextButtonPlayerActivity.setOnClickListener { songChange(clickNext = true) }
    }

    //Function to set current song name and image
    private fun setLayout(){
        Glide.with(this)
            .load(musicListPlayerActivity[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.harmony_logo_splash_screen).centerCrop())
            .into(binding.songImagePlayerActivity)
        binding.songNamePlayerActivity.text = musicListPlayerActivity[songPosition].title
    }

    //Function to create media player and handle exceptions
    private fun createMediaPlayer(){
        try {
            if(mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicListPlayerActivity[songPosition].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseButtonPlayerActivity.setIconResource(R.drawable.pause_icon)
        }catch (e: Exception){return}
    }

    //Function to initialize the layout
    private fun initializeLayout(){
        songPosition = intent.getIntExtra("index", 0)
        when(intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPlayerActivity = ArrayList()
                musicListPlayerActivity.addAll(MainActivity.MusicListMainActivity)
                setLayout()
                createMediaPlayer()
            }
        }
    }

    //Play button functionality
    private fun playMusic(){
        binding.playPauseButtonPlayerActivity.setIconResource(R.drawable.pause_icon)
        isPlaying = true
        mediaPlayer!!.start()
    }

    //Pause button functionality
    private fun pauseMusic(){
        binding.playPauseButtonPlayerActivity.setIconResource(R.drawable.play_icon)
        isPlaying = false
        mediaPlayer!!.pause()
    }

    //Previous and next button functionality
    private fun songChange(clickNext: Boolean) {
        if(clickNext) {
            setSongPosition(increase = true)
            setLayout()
            createMediaPlayer()
        }
        else {
            setSongPosition(increase = false)
            setLayout()
            createMediaPlayer()
        }
    }

    //Setting the song positions such that previous button on first song leads to last and vice versa
    private fun setSongPosition(increase: Boolean){
        if (increase) {
            if (musicListPlayerActivity.size -1 == songPosition)
                songPosition = 0
            else ++songPosition
        }
        else {
            if (0 == songPosition)
                songPosition = musicListPlayerActivity.size -1
            else --songPosition
        }
    }
}