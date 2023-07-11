package com.example.streamingservice.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.streamingservice.R
import com.example.streamingservice.databinding.FragmentFavoritesBinding
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.domain.entities.Playlist
import com.example.streamingservice.presentation.imageloader.DefaultImageLoader
import com.example.streamingservice.presentation.imageloader.ImageLoader
import com.example.streamingservice.presentation.view.OnMusicListener
import com.example.streamingservice.presentation.view.PlaylistCategoryAdapter
import com.example.streamingservice.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding ?: throw RuntimeException("FragmentFavoritesBinding == null")

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    private val playlistCategoryAdapter = PlaylistCategoryAdapter(object: OnMusicListener {
        override fun onMusicClick(position: String) {
            toPlaylistDetailFragment(getPlaylistById(position))
        }
    })
    private val imageLoader: ImageLoader = DefaultImageLoader()

    private val userPlaylistsLiveData: LiveData<List<Playlist>> by lazy {
        favoritesViewModel.getUserPlaylist()
    }
    private val userFavoritesLiveData: LiveData<List<Music>> by lazy {
        favoritesViewModel.getUserFavorites()
    }

    private var userPlaylistsList: MutableList<Playlist> = mutableListOf()
    private var userFavoritesList: MutableList<Music> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
        getUserPlaylists()

        binding.layoutFragmentFavoritesUserName.setOnClickListener {
            toUserProfileFragment()
        }
    }

    private fun onBind() {
        binding.textViewFragmentFavoritesUserName.text = favoritesViewModel.currentUser.username

        imageLoader.loadRecommendMusicPosterImg(
            context = requireContext(),
            url = favoritesViewModel.currentUser.imgLink,
            target = binding.textViewFragmentFavoritesUserIcon
        )

        binding.recyclerViewFragmentFavoritesUserPlaylists.adapter = playlistCategoryAdapter
    }

    private fun getUserPlaylists() {
        userFavoritesLiveData.observe(viewLifecycleOwner) {
            userFavoritesList.clear()
            userFavoritesList.addAll(it)
        }
        userPlaylistsLiveData.observe(viewLifecycleOwner) {
            userPlaylistsList.clear()
            userPlaylistsList.add(getFavoritePlaylist(userFavoritesList))
            userPlaylistsList.addAll(it)

            playlistCategoryAdapter.setList(userPlaylistsList)
        }
    }

    private fun getFavoritePlaylist(musics: List<Music>): Playlist {
        return Playlist(
            id = "0",
            playlistName = "Favorites",
            imageLink = "https://i.scdn.co/image/ab67706f000000025551996f500ba876bda73fa5",
            musics = musics
        )
    }

    private fun toUserProfileFragment() {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_container, UserProfileFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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

    private fun getPlaylistById(playlistId: String): Playlist? {
        var result: Playlist? = null
        for (playlist in userPlaylistsList) {
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