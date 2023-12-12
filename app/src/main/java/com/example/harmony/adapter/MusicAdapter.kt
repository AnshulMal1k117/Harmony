package com.example.harmony.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.harmony.R
import com.example.harmony.data.Music
import com.example.harmony.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private val musicList: ArrayList<Music>):RecyclerView.Adapter<MusicAdapter.MusicHolder>() {
    class MusicHolder(binding: MusicViewBinding):RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMusicView
        val album = binding.albumNameMusicView
        val image = binding.imageMusicView
        val duration = binding.songDurationMusicView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MusicHolder {
        return MusicHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MusicAdapter.MusicHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = musicList[position].duration.toString()
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.harmony_logo_splash_screen).centerCrop())
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}