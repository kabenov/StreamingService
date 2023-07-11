package com.example.streamingservice.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.exoplayer.ExoPlayer
import com.example.streamingservice.databinding.FragmentPlaylistDetailBinding
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist
import com.example.streamingservice.presentation.imageloader.DefaultImageLoader
import com.example.streamingservice.presentation.imageloader.ImageLoader
import com.example.streamingservice.presentation.view.OnMusicListener
import com.example.streamingservice.presentation.view.PlaylistDetailAdapter
import org.koin.android.ext.android.inject

class PlaylistDetailFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding: FragmentPlaylistDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentPlaylistDetailBinding == null")

    private var playlist: Playlist? = null

    private val audioPlayer: ExoPlayer by inject()

    private val playlistDetailAdapter = PlaylistDetailAdapter(object: OnMusicListener {
        override fun onMusicClick(position: String) {
            openPlayerActivity(getMusicById(position))
        }
    }, audioPlayer)

    private var playlistMusicDataList: MutableList<Music> = mutableListOf()

    private val imageLoader: ImageLoader = DefaultImageLoader()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = arguments?.getSerializable("playlist") as Playlist?

        binding.textViewFragmentPlaylistDetailBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        if(playlist != null) {
            playlistMusicDataList.addAll(playlist!!.musics)
        }

        binding.recyclerViewFragmentPlaylistDetailMusicList.adapter = playlistDetailAdapter
        playlistDetailAdapter.setList(playlistMusicDataList)

        imageLoader.loadRecommendMusicPosterImg(
            context = requireContext(),
            url = playlist?.imageLink ?: "https://i.scdn.co/image/ab67706f000000025551996f500ba876bda73fa5",
            target = binding.imageViewFragmentPlaylistDetailImage
        )
    }

    private fun getMusicById(musicId: String): Music? {
        var result: Music? = null
        for (music in playlistMusicDataList) {
            if(music.id == musicId) {
                result = music
            }
        }

        return result
    }

    private fun openPlayerActivity(music: Music?) {
        music.let {
            val intent = Intent(activity, PlayerActivity::class.java)
            intent.putExtra("music", it)
            println("MusicId: $it")
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}