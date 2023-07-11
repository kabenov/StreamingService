package com.example.streamingservice.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.streamingservice.R
import com.example.streamingservice.databinding.FragmentBottomPlayerViewBinding
import org.koin.android.ext.android.inject


class BottomPlayerViewFragment : Fragment(), Player.Listener {

    private var _binding: FragmentBottomPlayerViewBinding? = null
    private val binding: FragmentBottomPlayerViewBinding
        get() = _binding ?: throw RuntimeException("FragmentBottomPlayerViewBinding == null")

    private val audioPlayer: ExoPlayer by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomPlayerViewBinding.inflate(inflater, container, false)
        binding.root.visibility = View.GONE
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (PlayerActivity.musicService != null) {
            binding.root.visibility = View.VISIBLE
            onBind()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomPlayerLayoutMusicInfoLayout.setOnClickListener {
            val intent = Intent(activity, PlayerActivity::class.java)
            startActivity(intent)
        }

        binding.imageViewBottomPlayerLayoutPauseIc.setOnClickListener {
            if(audioPlayer.isPlaying) {
                audioPlayer.pause()
                binding.imageViewBottomPlayerLayoutPauseIc.setImageResource(R.drawable.ic_play)
                PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
            }
            else {
                audioPlayer.play()
                binding.imageViewBottomPlayerLayoutPauseIc.setImageResource(R.drawable.ic_pause)
                PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
            }
        }

        audioPlayer.addListener(this)
    }

    private fun onBind() {
        binding.textViewMusicInfoLayoutMusicTitle.text = audioPlayer.mediaMetadata.title
        binding.textViewMusicInfoLayoutMusicAuthor.text = audioPlayer.mediaMetadata.artist
        if(audioPlayer.isPlaying) {
            binding.imageViewBottomPlayerLayoutPauseIc.setImageResource(R.drawable.ic_pause)
            if(PlayerActivity.musicService != null) PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        }
        else {
            binding.imageViewBottomPlayerLayoutPauseIc.setImageResource(R.drawable.ic_play)
            if(PlayerActivity.musicService != null) PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)

        if (reason == Player.STATE_READY && !playWhenReady) {
            // Медиа было поставлено на паузу
            binding.imageViewBottomPlayerLayoutPauseIc.setImageResource(R.drawable.ic_play)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
            println("BottomPlayer listener to Pause")
        } else if (reason == Player.STATE_READY && playWhenReady) {
            // Медиа возобновляется после паузы
            binding.imageViewBottomPlayerLayoutPauseIc.setImageResource(R.drawable.ic_pause)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
            println("BottomPlayer listener to Play")
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        if(mediaItem != null && _binding != null) {
            onBind()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.removeListener(this)
        _binding = null
    }

}