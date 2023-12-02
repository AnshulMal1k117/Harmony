package com.example.harmony

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.content.ContextCompat
import com.example.harmony.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    /* REMEMBER TO ADD PERMISSIONS FOR API LEVELS BELOW 33 */

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Calling the function to check and ask permissions
        requestRuntimePermissions()

        //Setting the main theme of the app
        setTheme(R.style.Base_Theme_Harmony)

        //LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts
        binding = ActivityMainBinding.inflate(layoutInflater)

        //setting the main activity ast he default screen
        setContentView(binding.root)

        //Setting up the toggle
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        //Navigation Header intent
        binding.navigationButton.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.nav_feedback -> Toast.makeText(baseContext, "Feedback", Toast.LENGTH_SHORT).show()
                R.id.nav_settings -> Toast.makeText(baseContext, "Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_about -> Toast.makeText(baseContext, "About", Toast.LENGTH_SHORT).show()
                R.id.nav_exit -> exitProcess(1)
            }
            true
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

    //Handling what happens if permissions are granted or denied
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
}