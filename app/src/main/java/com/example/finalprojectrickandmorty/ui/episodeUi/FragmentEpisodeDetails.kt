package com.example.finalprojectrickandmorty.ui.episodeUi

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.adapter.episodeAdapter.EpisodeCharacterAdapter
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.example.finalprojectrickandmorty.ui.characterUi.FragmentCharacterMain
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_EPISODE_DETAILS
import com.example.finalprojectrickandmorty.util.EpisodeCharactersList
import com.example.finalprojectrickandmorty.viewModel.characterViewModel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentEpisodeDetails : Fragment(R.layout.fragment_episode_details) {

    private lateinit var binding: FragmentEpisodeDetailsBinding
    private lateinit var episode: EpisodeDatabaseEntity

    private val episodeDetailViewModel: CharacterViewModel by viewModels()

    private lateinit var itemCharacterOnClickListener: FragmentCharacterMain.ItemCharacterOnClickListener

    private val episodeCharacterAdapter by lazy {
        EpisodeCharacterAdapter { characterDatabaseEntity ->
            itemCharacterOnClickListener.onItemCharacterClicked(
                characterDatabaseEntity,
                KEY_FRAGMENT_EPISODE_DETAILS
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentCharacterMain.ItemCharacterOnClickListener) itemCharacterOnClickListener =
            context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEpisodeDetailsBinding.bind(view)
        getEpisodeFromArguments()
        bindViews()
        setting()
        lifecycleScope.launch {
            episodeDetailViewModel.dataSource.collectLatest {
                episodeCharacterAdapter.submitData(it)
            }
        }
        episodeCharacterAdapter.refresh()
    }

    private fun bindViews() {
        binding.textEpisodeDetailName.text = episode.name
        binding.textEpisodeDetailEpisode.text = episode.episode
        binding.textEpisodeDetailAirDate.text = episode.air_date
    }

    private fun setting() {
        binding.rvEpisodeCharacters.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = episodeCharacterAdapter
        }
        binding.ivEpisodeDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        episode.characters?.let {
            EpisodeCharactersList.episodeCharactersList.clear()
            EpisodeCharactersList.episodeCharactersList.addAll(it)
        }
    }

    private fun getEpisodeFromArguments() {
        arguments?.let { it ->
            it.getSerializable(KEY_EPISODE)?.let {
                if (it is EpisodeDatabaseEntity) {
                    episode = it
                } else {
                    error(getString(R.string.error_init_fragment_episode_details))
                }
            }
        } ?: error(getString(R.string.error_init_fragment_episode_details))
    }

    companion object {
        private const val KEY_EPISODE = "KEY_EPISODE"
        fun newInstance(episode: EpisodeDatabaseEntity?): FragmentEpisodeDetails {
            val bundle = bundleOf(KEY_EPISODE to episode)
            return FragmentEpisodeDetails().apply { arguments = bundle }
        }
    }

}