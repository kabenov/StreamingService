package com.example.streamingservice.presentation

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.streamingservice.R
import com.example.streamingservice.databinding.ActivityPlayerBinding
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.milliSecondsToTimer
import com.example.streamingservice.presentation.imageloader.DefaultImageLoader
import com.example.streamingservice.presentation.imageloader.ImageLoader
import com.example.streamingservice.service.MusicService
import org.koin.android.ext.android.inject


class PlayerActivity : AppCompatActivity(), Player.Listener, ServiceConnection {

    private var _binding: ActivityPlayerBinding? = null
    private val binding: ActivityPlayerBinding
        get() = _binding ?: throw RuntimeException("ActivityPlayerBinding == null")

    private val music: Music by lazy {
        intent.getSerializableExtra("music") as Music
    }
    private val imageLoader: ImageLoader = DefaultImageLoader()

    private val audioPlayer: ExoPlayer by inject()
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        preparePlayer()

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)

        handler.postDelayed(bindViewsRunnable, 800)

        updateSeekBar()
        audioPlayer.addListener(this)

        binding.seekBarActivityPlayer.setOnTouchListener { view, motionEvent ->
            view as SeekBar
            val playPosition = (audioPlayer.duration / 100) * view.progress
            audioPlayer.seekTo(playPosition)
            binding.textViewActivityPlayerCurrentTime.text = milliSecondsToTimer(audioPlayer.currentPosition)
            false
        }

        playerControls()

        binding.textViewActivityPlayerBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(bindViewsRunnable, 800)
        playerControls()
    }


    private fun preparePlayer() {
//        val audioUri = Uri.parse("http://stream.audioxi.com/SW")
//        val audioUri = Uri.parse("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/44/e9/00/44e90020-0b8e-a2a8-a10a-f522f2e49907/mzaf_13950408475320535247.plus.aac.ep.m4a")
        val audioUri = Uri.parse(music.uri)
        val mediaItem: MediaItem = MediaItem.fromUri(audioUri)
        audioPlayer.setMediaItem(mediaItem)

        audioPlayer.prepare()

        audioPlayer.playWhenReady = true
        audioPlayer.play()
    }

    private val bindViewsRunnable = Runnable {
        bindViews()
    }

    private fun bindViews() {
        with(binding) {
            val title = audioPlayer.mediaMetadata.title
            val author = audioPlayer.mediaMetadata.artist
            val posterUrl = audioPlayer.mediaMetadata.artworkUri
            val duration = milliSecondsToTimer(audioPlayer.duration)

//            imageViewActivityPlayerPoster.setImageResource(R.drawable.music_player_poster)

            textViewActivityPlayerMusicTitle.text = title
            textViewActivityPlayerMusicAuthor.text = author
            textViewActivityPlayerMusicDuration.text = duration

            if (posterUrl != null) {
                imageLoader.loadRecommendMusicPosterImg(
                    context = binding.imageViewActivityPlayerPoster.context,
                    url = posterUrl,
                    target = binding.imageViewActivityPlayerPoster
                )
            }

            seekBarActivityPlayer.max = 100

            if (audioPlayer.isPlaying) {
                imageViewActivityPlayerPlayPause.setImageResource(R.drawable.ic_pause)
                musicService!!.showNotification(R.drawable.ic_pause)
            }
            else {
                imageViewActivityPlayerPlayPause.setImageResource(R.drawable.ic_play)
                musicService!!.showNotification(R.drawable.ic_play)
            }
        }
    }

    private fun playerControls() {
        binding.imageViewActivityPlayerPlayPause.setOnClickListener {
            if(audioPlayer.isPlaying) {
                audioPlayer.pause()
                binding.imageViewActivityPlayerPlayPause.setImageResource(R.drawable.ic_play)

                musicService!!.showNotification(R.drawable.ic_play)

                binding.textViewActivityPlayerMusicDuration.text = milliSecondsToTimer(audioPlayer.duration)
            }
            else {
                audioPlayer.play()
                binding.imageViewActivityPlayerPlayPause.setImageResource(R.drawable.ic_pause)

                musicService!!.showNotification(R.drawable.ic_pause)

                binding.textViewActivityPlayerMusicDuration.text = milliSecondsToTimer(audioPlayer.duration)
            }
        }

        binding.textViewActivityPlayerNext.setOnClickListener {
            if(audioPlayer.hasNextMediaItem()) {
                audioPlayer.seekToNext()
                musicService!!.showNotification(R.drawable.ic_pause)
            }
        }
        binding.textViewActivityPlayerPrevious.setOnClickListener {
            if(audioPlayer.hasPreviousMediaItem()) {
                audioPlayer.seekToPrevious()
                musicService!!.showNotification(R.drawable.ic_pause)
            }
        }
    }

    private val updater = Runnable {
        updateSeekBar()
        val currentDuration = audioPlayer.currentPosition
        binding.textViewActivityPlayerCurrentTime.text = milliSecondsToTimer(currentDuration)
    }

    private fun updateSeekBar() {
        if(audioPlayer.isPlaying) {
            binding.seekBarActivityPlayer.progress = ((audioPlayer.currentPosition.toFloat() / audioPlayer.duration) * 100).toInt()
            binding.seekBarActivityPlayer.secondaryProgress = ((audioPlayer.bufferedPosition.toFloat() / audioPlayer.duration) * 100).toInt()
            handler.postDelayed(updater, 500)
            println("Updater")
        }
    }

    private fun defaultSeekBar() {
        binding.seekBarActivityPlayer.progress = ((0.toFloat() / audioPlayer.duration) * 100).toInt()
        binding.seekBarActivityPlayer.secondaryProgress = ((audioPlayer.bufferedPosition.toFloat() / audioPlayer.duration) * 100).toInt()
        binding.textViewActivityPlayerCurrentTime.text = milliSecondsToTimer(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updater)
        audioPlayer.removeListener(this)
        _binding = null
    }

    // ----------------------- Player Listeners ----------------------- \\

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        if(mediaItem != null && _binding != null) {
            bindViews()
            handler.removeCallbacks(updater)
            defaultSeekBar()
            updateSeekBar()
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if(_binding != null) {
            if (playbackState == Player.STATE_READY || audioPlayer.currentMediaItem != null) {
                bindViews()
                handler.removeCallbacks(updater)
                updateSeekBar()
            }
            if (playbackState == Player.STATE_ENDED) {
                binding.seekBarActivityPlayer.progress = 0
                binding.textViewActivityPlayerCurrentTime.text =
                    R.string.activity_player_default_current_time.toString()
                binding.textViewActivityPlayerMusicDuration.text =
                    R.string.activity_player_default_music_duration.toString()
            }
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)

        if (reason == Player.STATE_IDLE && !playWhenReady && _binding != null) {
            // Медиа было поставлено на паузу
            binding.imageViewActivityPlayerPlayPause.setImageResource(R.drawable.ic_play)
            handler.removeCallbacks(updater)
            updateSeekBar()

            musicService!!.showNotification(R.drawable.ic_play)
            println("PlayerActivity listener to Pause")

            binding.textViewActivityPlayerMusicDuration.text = milliSecondsToTimer(audioPlayer.duration)
        } else if (reason == Player.STATE_IDLE && playWhenReady && _binding != null) {
            // Медиа возобновляется после паузы
            binding.imageViewActivityPlayerPlayPause.setImageResource(R.drawable.ic_pause)
            handler.removeCallbacks(updater)
            updateSeekBar()

            musicService!!.showNotification(R.drawable.ic_pause)
            println("PlayerActivity listener to Play")

            binding.textViewActivityPlayerMusicDuration.text = milliSecondsToTimer(audioPlayer.duration)
        }
    }


    // ----------------------- ServiceConnection ----------------------- \\

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if(musicService == null){
            val binder = service as MusicService.MyBinder
            musicService = binder.currentService()
            musicService!!.showNotification(R.drawable.ic_pause)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    companion object {
        var musicService: MusicService? = null
    }
}