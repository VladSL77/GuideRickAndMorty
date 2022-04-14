package com.example.finalprojectrickandmorty.ui.locationUi

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.adapter.locationAdapter.LocationAdapter
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.util.Constants
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_LOCATION_MAIN
import com.example.finalprojectrickandmorty.util.LocationSearchParams
import com.example.finalprojectrickandmorty.util.StateWarnings
import com.example.finalprojectrickandmorty.viewModel.locationViewModel.LocationSearchViewModel
import com.example.finalprojectrickandmorty.viewModel.locationViewModel.LocationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentLocationMain : Fragment(R.layout.fragment_location_main) {

    private val locationViewModel: LocationViewModel by activityViewModels()
    private val viewModel: LocationSearchViewModel by activityViewModels()

    private lateinit var rvFragment: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonSearch: Button
    private lateinit var buttonReloadData: FloatingActionButton

    private lateinit var itemLocationOnClickListener: ItemLocationOnClickListener

    private val locationAdapter by lazy {
        LocationAdapter { locationDatabaseEntity ->
            itemLocationOnClickListener.onItemLocationClicked(
                locationDatabaseEntity,
                KEY_FRAGMENT_LOCATION_MAIN
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemLocationOnClickListener) itemLocationOnClickListener = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setting()
        lifecycleScope.launch {
            locationViewModel.dataSource.collectLatest {
                locationAdapter.submitData(it)
            }
        }
        reloadData()
    }

    interface ItemLocationOnClickListener {
        fun onItemLocationClicked(location: LocationDatabaseEntity, tag: String)
    }

    private fun init() {
        buttonSearch = requireView().findViewById(R.id.button_search)
        buttonReloadData = requireView().findViewById(R.id.floatingActionButton)
        rvFragment = requireView().findViewById(R.id.rv_location_main)
        progressBar = requireView().findViewById(R.id.progress_bar)
    }

    private fun setting() {
        buttonSearch.setOnClickListener {
            FragmentLocationSearch().show(
                childFragmentManager,
                Constants.KEY_FRAGMENT_LOCATION_SEARCH
            )
        }
        buttonReloadData.setOnClickListener {
            reloadData()
        }
        rvFragment.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = locationAdapter
        }
        locationAdapter.addLoadStateListener { loadStates ->
            rvFragment.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            buttonSearch.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            buttonReloadData.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            progressBar.isVisible = loadStates.mediator?.refresh is LoadState.Loading
            handleWarnings()
            resetWarnings()
        }
        viewModel.selectedItem.observe(viewLifecycleOwner) {
            locationAdapter.refresh()
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
        resetLocationParams()
        resetWarnings()
        locationAdapter.refresh()
    }

    private fun resetWarnings() {
        StateWarnings.networkWarning = false
        StateWarnings.emptyDataWarning = false
    }

    private fun resetLocationParams() {
        LocationSearchParams.name = ""
        LocationSearchParams.type = ""
        LocationSearchParams.dimension = ""
    }

}