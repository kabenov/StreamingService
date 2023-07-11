package com.example.streamingservice.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import com.example.streamingservice.R
import com.example.streamingservice.StreamingServiceApplication
import com.example.streamingservice.presentation.PlayerActivity
import org.koin.java.KoinJavaComponent.get
import kotlin.system.exitProcess

class NotificationReceiver: BroadcastReceiver() {
    private val audioPlayer: ExoPlayer = get(ExoPlayer::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            StreamingServiceApplication.PLAYER_PLAY -> if (audioPlayer.isPlaying) pauseMusic() else playMusic()
            StreamingServiceApplication.PLAYER_NEXT -> playerNext()
            StreamingServiceApplication.PLAYER_PREVIOUS -> playerPrevious()
            StreamingServiceApplication.PLAYER_EXIT -> playerExit()
        }
    }
    private fun pauseMusic() {
        audioPlayer.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        //for handling app crash during notification play - pause btn (While app opened through intent)
//        try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon) }catch (_: Exception){}
    }

    private fun playMusic(){
        audioPlayer.play()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        //for handling app crash during notification play - pause btn (While app opened through intent)
//        try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon) }catch (_: Exception){}
    }

    private fun playerPrevious() {
        if(audioPlayer.hasPreviousMediaItem()) audioPlayer.seekToPrevious()

        if(audioPlayer.isPlaying) PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        else PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)

    }

    private fun playerNext() {
        if(audioPlayer.hasNextMediaItem()) audioPlayer.seekToNext()

        if(audioPlayer.isPlaying) PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        else PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
    }

    private fun playerExit() {
        PlayerActivity.musicService!!.stopForeground(true)
        PlayerActivity.musicService = null
        if(audioPlayer.isPlaying) {
            audioPlayer.stop()
        }
        audioPlayer.release()
        exitProcess(1)
    }

//    private fun prevNextSong(increment: Boolean, context: Context){
//        setSongPosition(increment = increment)
//        PlayerActivity.musicService!!.createMediaPlayer()
//        Glide.with(context)
//            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
//            .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
//            .into(PlayerActivity.binding.songImgPA)
//        PlayerActivity.binding.songNamePA.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
//        Glide.with(context)
//            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
//            .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
//            .into(NowPlaying.binding.songImgNP)
//        NowPlaying.binding.songNameNP.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
//        playMusic()
//        PlayerActivity.fIndex = favouriteChecker(PlayerActivity.musicListPA[PlayerActivity.songPosition].id)
//        if(PlayerActivity.isFavourite) PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_icon)
//        else PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_empty_icon)
//    }
}