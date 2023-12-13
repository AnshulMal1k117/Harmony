package com.example.harmony

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.harmony.data.Music
import com.example.harmony.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    companion object{
        lateinit var musicListPlayerActivity: ArrayList<Music>
        var songPosition:Int = 0
        var mediaPlayer: MediaPlayer? = null
    }

    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_Harmony)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        songPosition = intent.getIntExtra("index", 0)
        when(intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPlayerActivity = ArrayList()
                musicListPlayerActivity.addAll(MainActivity.MusicListMainActivity)
                if(mediaPlayer == null) mediaPlayer = MediaPlayer()
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(musicListPlayerActivity[songPosition].path)
            }
        }

    }
}