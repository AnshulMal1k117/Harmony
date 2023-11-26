package com.example.harmony

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.harmony.databinding.ActivityPlaylistsBinding

class PlaylistsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_Harmony)
        binding = ActivityPlaylistsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}