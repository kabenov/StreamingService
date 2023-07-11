package com.example.streamingservice.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streamingservice.databinding.FragmentAudioBookBinding
import com.example.streamingservice.domain.entities.Audiobook
import com.example.streamingservice.domain.entities.Genre
import com.example.streamingservice.domain.entities.Music
import com.example.streamingservice.presentation.view.*
import com.example.streamingservice.presentation.viewmodel.AudiobookViewModel
import com.example.streamingservice.presentation.viewmodel.HomePageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudiobookFragment : Fragment() {

    private var _binding: FragmentAudioBookBinding? = null
    private val binding: FragmentAudioBookBinding
        get() = _binding ?: throw RuntimeException("FragmentAudioBookBinding == null")


    private val audiobookCategoryAdapter = AudiobookCategoryAdapter(object: OnMusicListener {
        override fun onMusicClick(position: String) {

        }
    })
//    private val genresAdapter = GenreCategoryAdapter(object: OnGenreListener {
//        override fun onGenreClick(position: Long) {
//
//        }
//    })

    private val audiobookViewModel: AudiobookViewModel by viewModel()

    private val allAudiobookDataListLiveData: LiveData<List<Audiobook>> by lazy {
        audiobookViewModel.getAllAudiobookDataList()
    }
    private var audiobookRecommendDataList: MutableList<Audiobook> = mutableListOf()
    private var genresDataList: MutableList<Genre> = mutableListOf()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()
//        getMusicList()
//        getGenreList()
//        displayToScreen()
        binding.genreCategoryMusic.recyclerViewFragmentHomePageGenres.layoutManager = GridLayoutManager(context, 2)
    }

    private fun onBind() {
        binding.musicCategoryRecommended.recyclerViewFragmentHomePageRecommended.adapter = audiobookCategoryAdapter
//        binding.genreCategoryMusic.recyclerViewFragmentHomePageGenres.adapter = genresAdapter
    }

//    private fun getMusicList() {
//        allAudiobookDataListLiveData.observe(viewLifecycleOwner) {
//            audiobookRecommendDataList.addAll(it)
//            audiobookCategoryAdapter.setList(audiobookRecommendDataList)
//        }
//    }

//    private fun getGenreList() {
//        genresDataList.addAll(audiobookViewModel.getAudiobookGenreDataList())
//    }

//    private fun displayToScreen() {
//        genresAdapter.setList(genresDataList)
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}