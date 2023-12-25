package com.example.harmony.data

import android.media.MediaMetadataRetriever
import com.example.harmony.ui.PlayerActivity
import java.util.concurrent.TimeUnit

data class Music(val id:String, val title:String, val album:String, val artist:String, val path:String, val duration:Long=0,
val artUri:String)

fun formatDuration(duration:Long):String{
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}

fun getImageArt(path:String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

//Setting the song positions such that previous button on first song leads to last and vice versa
fun setSongPosition(increase: Boolean){
    if (increase) {
        if (PlayerActivity.musicListPlayerActivity.size -1 == PlayerActivity.songPosition)
            PlayerActivity.songPosition = 0
        else ++PlayerActivity.songPosition
    }
    else {
        if (0 == PlayerActivity.songPosition)
            PlayerActivity.songPosition = PlayerActivity.musicListPlayerActivity.size -1
        else --PlayerActivity.songPosition
    }
}