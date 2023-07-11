package com.example.streamingservice.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streamingservice.R
import com.example.streamingservice.databinding.FragmentHomePageBinding
import com.example.streamingservice.domain.entities.Genre
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist
import com.example.streamingservice.presentation.view.*
import com.example.streamingservice.presentation.viewmodel.HomePageViewModel
import com.example.streamingservice.presentation.viewmodel.SignUpViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding: FragmentHomePageBinding
        get() = _binding ?: throw RuntimeException("FragmentHomePageBinding == null")

    private val audioPlayer: ExoPlayer by inject()
    private val homePageViewModel: HomePageViewModel by viewModel()

    private val musicRecommendAdapter = MusicCategoryAdapter(object: OnMusicListener{
        override fun onMusicClick(position: String) {
            openPlayerActivity(getMusicById(position))
        }
    }, audioPlayer)
    private val playlistMusicRecommendedAdapter = HomePlaylistCategoryAdapter(object: OnMusicListener{
        override fun onMusicClick(position: String) {
            toPlaylistDetailFragment(getPlaylistById(position, playlist1))
        }
    })
    private val playlistMusicAdapter = HomePlaylistCategoryAdapter(object: OnMusicListener{
        override fun onMusicClick(position: String) {
            toPlaylistDetailFragment(getPlaylistById(position, playlist2))
        }
    })


    private val musicRecommendLiveDataList: LiveData<List<Music>> by lazy {
        homePageViewModel.getMusicDataList()
    }

    private val allPlaylistLiveDataList: LiveData<List<Playlist>> by lazy {
        homePageViewModel.getAllPlaylist()
    }

    private var musicRecommendDataList: MutableList<Music> = mutableListOf()
    private var allPlaylistDataList: MutableList<Playlist> = mutableListOf()

    private var playlist1: MutableList<Playlist> = mutableListOf()
    private var playlist2: MutableList<Playlist> = mutableListOf()

    private var genresDataList: MutableList<Genre> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
        getMusicList()
        getGenreList()
//        displayToScreen()
//        binding.genreCategoryMusic.recyclerViewFragmentHomePageGenres.layoutManager = GridLayoutManager(context, 2)
    }

    private fun onBind() {
        binding.musicCategoryRecommended.recyclerViewFragmentHomePageRecommended.adapter = musicRecommendAdapter
        binding.musicPlaylistRecommended.recyclerViewFragmentHomePageRecommended.adapter = playlistMusicRecommendedAdapter
        val string1 = "Ең үздік"
        binding.musicPlaylistRecommended.textViewFragmentHomePageRecycleTitleReccommend.text = string1
        binding.musicPlaylist.recyclerViewFragmentHomePageRecommended.adapter = playlistMusicAdapter
        val string2 = "Тек сізге"
        binding.musicPlaylist.textViewFragmentHomePageRecycleTitleReccommend.text = string2
//        binding.genreCategoryMusic.recyclerViewFragmentHomePageGenres.adapter = genresAdapter
    }

    private fun getMusicList() {
        musicRecommendLiveDataList.observe(viewLifecycleOwner) {
            musicRecommendDataList.addAll(it)

            musicRecommendAdapter.setList(musicRecommendDataList)
        }
        allPlaylistLiveDataList.observe(viewLifecycleOwner) {
            allPlaylistDataList.addAll(it)
            playlist1.add(allPlaylistDataList[0])
            playlist1.add(allPlaylistDataList[1])
            playlist1.add(allPlaylistDataList[2])
            playlist1.add(allPlaylistDataList[3])

            playlist2.add(allPlaylistDataList[2])
            playlist2.add(allPlaylistDataList[3])
            playlist2.add(allPlaylistDataList[4])
            playlistMusicRecommendedAdapter.setList(playlist1)
            playlistMusicAdapter.setList(playlist2)
        }
    }

    private fun getGenreList() {
        genresDataList.addAll(homePageViewModel.getGenreDataList())
    }

//    private fun displayToScreen() {
//        musicRecommendAdapter.setList(musicRecommendDataList)
//        genresAdapter.setList(genresDataList)
//    }

    private fun getMusicById(musicId: String): Music? {
        var result: Music? = null
        for (music in musicRecommendDataList) {
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

    private fun toPlaylistDetailFragment(playlist: Playlist?) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val playlistDetailFragment = PlaylistDetailFragment()
        val bundle = Bundle()
        bundle.putSerializable("playlist", playlist)
        playlistDetailFragment.arguments = bundle

        fragmentTransaction.replace(R.id.main_container, playlistDetailFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun getPlaylistById(playlistId: String, playlistList: List<Playlist>): Playlist? {
        var result: Playlist? = null
        for (playlist in playlistList) {
            if(playlist.id == playlistId) {
                result = playlist
            }
        }

        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}