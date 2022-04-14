package com.example.finalprojectrickandmorty.ui.episodeUi

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.adapter.episodeAdapter.EpisodeAdapter
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.util.Constants
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_EPISODE_MAIN
import com.example.finalprojectrickandmorty.util.EpisodesSearchParams
import com.example.finalprojectrickandmorty.util.StateWarnings
import com.example.finalprojectrickandmorty.viewModel.episodeViewModel.EpisodeSearchViewModel
import com.example.finalprojectrickandmorty.viewModel.episodeViewModel.EpisodeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentEpisodeMain : Fragment(R.layout.fragment_episode_main) {


    private val episodeViewModel: EpisodeViewModel by viewModels()
    private val viewModel: EpisodeSearchViewModel by activityViewModels()

    private lateinit var rvFragment: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonSearch: Button
    private lateinit var buttonReloadData: FloatingActionButton

    private lateinit var itemEpisodeOnClickListener: ItemEpisodeOnClickListener

    private val episodeAdapter by lazy {
        EpisodeAdapter { episodeDatabaseEntity ->
            itemEpisodeOnClickListener.onItemEpisodeClicked(
                episodeDatabaseEntity,
                KEY_FRAGMENT_EPISODE_MAIN
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemEpisodeOnClickListener) itemEpisodeOnClickListener = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setting()
        lifecycleScope.launch {
            episodeViewModel.dataSource.collectLatest {
                episodeAdapter.submitData(it)
            }
        }
    }

    interface ItemEpisodeOnClickListener {
        fun onItemEpisodeClicked(episode: EpisodeDatabaseEntity, tag: String)
    }

    private fun init() {
        buttonSearch = requireView().findViewById(R.id.button_search)
        buttonReloadData = requireView().findViewById(R.id.floatingActionButton)
        rvFragment = requireView().findViewById(R.id.rv_episode_main)
        progressBar = requireView().findViewById(R.id.progress_bar)
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    private fun setting() {
        buttonSearch.setOnClickListener {
            FragmentEpisodeSearch().show(
                childFragmentManager,
                Constants.KEY_FRAGMENT_EPISODE_SEARCH
            )
        }
        buttonReloadData.setOnClickListener {
            reloadData()
        }
        rvFragment.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = episodeAdapter
        }
        episodeAdapter.addLoadStateListener { loadStates ->
            rvFragment.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            buttonSearch.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            buttonReloadData.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            progressBar.isVisible = loadStates.mediator?.refresh is LoadState.Loading
            handleWarnings()
            resetWarnings()
        }
        viewModel.selectedItem.observe(viewLifecycleOwner) {
            episodeAdapter.refresh()
        }
    }

    private fun handleWarnings() {
        if (StateWarnings.networkWarning) {
            Toast.makeText(
                requireContext(),
                getString(R.string.network_warning),
                Toast.LENGTH_LONG
            ).show()
            StateWarnings.networkWarning = false
        }
        if (StateWarnings.emptyDataWarning) {
            Toast.makeText(
                requireContext(),
                getString(R.string.query_warning),
                Toast.LENGTH_LONG
            ).show()
            StateWarnings.emptyDataWarning = false
        }
    }

    private fun reloadData() {
        resetSearchParams()
        resetWarnings()
        episodeAdapter.refresh()
    }

    private fun resetWarnings() {
        StateWarnings.networkWarning = false
        StateWarnings.emptyDataWarning = false
    }

    private fun resetSearchParams() {
        EpisodesSearchParams.name = ""
        EpisodesSearchParams.episode = ""
        EpisodesSearchParams.airDate = ""
    }

}