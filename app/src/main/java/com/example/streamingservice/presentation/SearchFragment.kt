package com.example.streamingservice.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.exoplayer.ExoPlayer
import com.example.streamingservice.databinding.FragmentSearchBinding
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.presentation.view.OnMusicListener
import com.example.streamingservice.presentation.view.SearchedMusicAdapter
import com.example.streamingservice.presentation.viewmodel.SearchViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")

    private val audioPlayer: ExoPlayer by inject()
    private val searchViewModel: SearchViewModel by viewModel()

    private var searchedMusicDataList: MutableList<Music> = mutableListOf()
    private var searchResultLiveData: LiveData<List<Music>> = MutableLiveData()

    private val searchedMusicAdapter = SearchedMusicAdapter(object: OnMusicListener {
        override fun onMusicClick(position: String) {
            openPlayerActivity(getMusicById(position))
        }
    }, audioPlayer)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFragmentSearchResultList.adapter = searchedMusicAdapter

        binding.textViewFragmentSearchButton.setOnClickListener {
            searchResultLiveData = searchViewModel.multiSearch(binding.editTextFragmentSearch.text.toString())
            observeSearchResultLiveData()
        }
    }

    private fun observeSearchResultLiveData() {
        searchResultLiveData.observe(viewLifecycleOwner) {
            searchedMusicDataList.addAll(it)
            searchedMusicAdapter.setList(searchedMusicDataList)
        }
    }


    private fun getMusicById(musicId: String): Music? {
        var result: Music? = null
        for (music in searchedMusicDataList) {
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