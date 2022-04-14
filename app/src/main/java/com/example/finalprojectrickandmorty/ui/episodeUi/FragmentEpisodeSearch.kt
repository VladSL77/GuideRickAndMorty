package com.example.finalprojectrickandmorty.ui.episodeUi

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.util.EpisodesSearchParams
import com.example.finalprojectrickandmorty.viewModel.episodeViewModel.EpisodeSearchViewModel


class FragmentEpisodeSearch : DialogFragment(R.layout.fragment_episode_search) {


    private lateinit var ivSearchBack: ImageView
    private lateinit var etName: EditText
    private lateinit var etEpisode: EditText
    private lateinit var etAirDate: EditText
    private lateinit var buttonRunSearch: Button

    private val episodeSearchViewModel: EpisodeSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setting()
    }

    private fun init() {
        ivSearchBack = requireView().findViewById(R.id.iv_episode_detail_back)
        etName = requireView().findViewById(R.id.et_episode_name)
        etEpisode = requireView().findViewById(R.id.et_episode_episode)
        etAirDate = requireView().findViewById(R.id.et_episode_air_date)
        buttonRunSearch = requireView().findViewById(R.id.button_episode_run_search)
    }

    private fun setting() {
        ivSearchBack.setOnClickListener {
            dismiss()
        }
        buttonRunSearch.setOnClickListener {
            createSearchParams()
            episodeSearchViewModel.selectItem(getString(R.string.start_search))
            dismiss()
        }
    }

    private fun createSearchParams() {
        EpisodesSearchParams.name = etName.text.toString()
        EpisodesSearchParams.episode = etEpisode.text.toString()
        EpisodesSearchParams.airDate = etAirDate.text.toString()
    }

}