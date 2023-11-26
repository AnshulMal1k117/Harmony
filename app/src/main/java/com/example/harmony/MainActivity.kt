package com.example.harmony

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.content.ContextCompat
import com.example.harmony.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //REMEMBER TO ADD PERMISSIONS FOR API LEVELS BELOW 33

    private lateinit var binding: ActivityMainBinding
    private lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRuntimePermissions()
        setTheme(R.style.Base_Theme_Harmony)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //Initializing the toogle
        toogle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        setContentView(binding.root)

        //Shuffle Button
        binding.shuffleButton.setOnClickListener {
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            startActivity(intent)
        }

        //Favourites button intent
        binding.favouritesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavouritesActivity::class.java))
        }

        //Playlists button intent
        binding.playlistsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlaylistsActivity::class.java))
        }
    }

    //To request permissions
    private fun requestRuntimePermissions() {
        if (checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
            !=PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES), 10)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {

            //Checking Permission for audio access
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Audio Access Granted", Toast.LENGTH_SHORT).show()
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_MEDIA_AUDIO), 10)

            //Checking Permission for image access
            if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Image Access Granted", Toast.LENGTH_SHORT).show()
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES), 10)
        }
    }
}