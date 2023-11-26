package com.example.harmony

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.harmony.databinding.ActivityFavouritesBinding

class FavouritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_Harmony)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}