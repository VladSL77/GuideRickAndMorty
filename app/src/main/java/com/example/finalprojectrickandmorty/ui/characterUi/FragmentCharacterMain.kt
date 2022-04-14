package com.example.finalprojectrickandmorty.ui.characterUi

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
import com.example.finalprojectrickandmorty.adapter.characterAdapter.CharacterAdapter
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.util.CharactersSearchParams
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_CHARACTER_MAIN
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_CHARACTER_SEARCH
import com.example.finalprojectrickandmorty.util.StateWarnings
import com.example.finalprojectrickandmorty.viewModel.characterViewModel.CharacterSearchViewModel
import com.example.finalprojectrickandmorty.viewModel.characterViewModel.CharacterViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentCharacterMain : Fragment(R.layout.fragment_character_main) {

    private val characterViewModel: CharacterViewModel by viewModels()
    private val viewModel: CharacterSearchViewModel by activityViewModels()

    private lateinit var rvFragment: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonSearch: Button
    private lateinit var buttonReloadData: FloatingActionButton

    private lateinit var itemCharacterOnClickListener: ItemCharacterOnClickListener

    private val characterAdapter by lazy {
        CharacterAdapter { characterDatabaseEntity ->
            itemCharacterOnClickListener.onItemCharacterClicked(
                characterDatabaseEntity,
                KEY_FRAGMENT_CHARACTER_MAIN
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemCharacterOnClickListener) itemCharacterOnClickListener = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setting()
        lifecycleScope.launch {
            characterViewModel.dataSource.collectLatest {
                characterAdapter.submitData(it)
            }
        }
    }

    interface ItemCharacterOnClickListener {
        fun onItemCharacterClicked(character: CharacterDatabaseEntity, tag: String)
    }

    override fun onResume() {
        super.onResume()
        characterAdapter.refresh()
    }

    private fun init() {
        buttonSearch = requireView().findViewById(R.id.button_search)
        buttonReloadData = requireView().findViewById(R.id.floatingActionButton)
        rvFragment = requireView().findViewById(R.id.rv_character_main)
        progressBar = requireView().findViewById(R.id.progress_bar)
    }

    private fun setting() {
        buttonSearch.setOnClickListener {
            FragmentCharacterSearch().show(childFragmentManager, KEY_FRAGMENT_CHARACTER_SEARCH)
        }
        buttonReloadData.setOnClickListener {
            reloadData()
        }
        rvFragment.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = characterAdapter
        }
        characterAdapter.addLoadStateListener { loadStates ->
            rvFragment.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            buttonSearch.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            buttonReloadData.isVisible = loadStates.mediator?.refresh is LoadState.NotLoading
            progressBar.isVisible = loadStates.mediator?.refresh is LoadState.Loading
            handleWarnings()
        }
        viewModel.selectedItem.observe(viewLifecycleOwner) {
            characterAdapter.refresh()
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
        characterAdapter.refresh()
    }

    private fun resetSearchParams() {
        CharactersSearchParams.name = ""
        CharactersSearchParams.status = ""
        CharactersSearchParams.species = ""
        CharactersSearchParams.type = ""
        CharactersSearchParams.gender = ""
    }

}