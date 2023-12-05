package com.example.harmony.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harmony.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private val musicList: ArrayList<String>):RecyclerView.Adapter<MusicAdapter.MusicHolder>() {
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
        holder.title.text = musicList[position]
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}