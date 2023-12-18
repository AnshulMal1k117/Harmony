package com.example.harmony

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harmony.adapter.MusicAdapter
import com.example.harmony.data.Music
import com.example.harmony.databinding.ActivityMainBinding
import com.example.harmony.ui.FavouritesActivity
import com.example.harmony.ui.PlayerActivity
import com.example.harmony.ui.PlaylistsActivity
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    /*ADD PERMISSIONS FOR API LEVELS BELOW 33-: Done
    * FIX THE BUG IN SPLASH SCREEN ON LOWER API LEVELS
    * FIXED A BUG WHERE THE APP WOULD CRASH BECAUSE THE CODE TO LOAD MUSIC EXECUTED BEFORE PERMISSIONS WERE GRANTED
    * FIXED ANOTHER BUG IN getAllAudio() WHERE SOME title, iD, album or artist WOULD RETURN NULL, USING ELVIS OPERATOR  */

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicAdapter

    companion object {
        lateinit var MusicListMainActivity: ArrayList<Music>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting the main theme of the app to Navigation Drawer Theme with action bar
        setTheme(R.style.Navigation_Drawer_Theme)

        //LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts
        binding = ActivityMainBinding.inflate(layoutInflater)

        //setting the main activity ast he default screen
        setContentView(binding.root)

        //Setting up the toggle
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(requestRuntimePermissions()) {
            initializeLayout()
        }

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



    private fun requestRuntimePermissions() :Boolean{
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
                return false
            }
        }
        //android 13 permission request
        else if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), 13)
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 13){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted",Toast.LENGTH_SHORT).show()
                initializeLayout()
            }
            else
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    //Function to initialize layout
    private fun initializeLayout(){
        MusicListMainActivity = getAllAudio()
        binding.musicRecyclerView.setHasFixedSize(true)
        binding.musicRecyclerView.setItemViewCacheSize(10)
        binding.musicRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        musicAdapter = MusicAdapter(this@MainActivity, MusicListMainActivity)
        binding.musicRecyclerView.adapter = musicAdapter
        val totalSongs:String = "Total Songs: " +musicAdapter.itemCount
        binding.totalSongs.text = totalSongs

    }

    //Function to get the audio files in the player using cursor
    @SuppressLint("Recycle", "Range")
    private fun getAllAudio(): ArrayList<Music> {
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID)
        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection
            , null, MediaStore.Audio.Media.DATE_ADDED + " DESC", null)
        if (cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    //suppressed range with annotation
                    val titleCursor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))?:"Unknown"
                    val idCursor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))?:"Unknown"
                    val albumCursor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))?:"Unknown"
                    val artistCursor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))?:"Unknown"
                    val pathCursor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationCursor = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIDCursor= cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriCursor = Uri.withAppendedPath(uri, albumIDCursor).toString()
                    val music = Music(id = idCursor, title = titleCursor, album = albumCursor,
                        artist = artistCursor, path = pathCursor, duration = durationCursor,
                        artUri = artUriCursor)
                    val file = File(music.path)
                    if (file.exists())
                        tempList.add(music)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return tempList
    }
}