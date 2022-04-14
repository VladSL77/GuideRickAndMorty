package com.example.finalprojectrickandmorty.ui.locationUi

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.adapter.locationAdapter.LocationResidentAdapter
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.FragmentLocationDetailsBinding
import com.example.finalprojectrickandmorty.ui.characterUi.FragmentCharacterMain
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_LOCATION_DETAILS
import com.example.finalprojectrickandmorty.util.LocationResidentsList
import com.example.finalprojectrickandmorty.viewModel.characterViewModel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentLocationDetails : Fragment(R.layout.fragment_location_details) {

    private lateinit var binding: FragmentLocationDetailsBinding
    private lateinit var location: LocationDatabaseEntity

    private val locationDetailViewModel: CharacterViewModel by activityViewModels()

    private lateinit var itemCharacterOnClickListener: FragmentCharacterMain.ItemCharacterOnClickListener

    private val locationResidentsAdapter by lazy {
        LocationResidentAdapter { characterDatabaseEntity ->
            itemCharacterOnClickListener.onItemCharacterClicked(
                characterDatabaseEntity,
                KEY_FRAGMENT_LOCATION_DETAILS
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
        binding = FragmentLocationDetailsBinding.bind(view)
        getLocationFromArguments()
        bindViews()
        setting()
        lifecycleScope.launch {
            locationDetailViewModel.dataSource.collectLatest {
                locationResidentsAdapter.submitData(it)
            }
        }
        locationResidentsAdapter.refresh()
    }

    private fun bindViews() {
        binding.textLocationDetailName.text = location.name
        binding.textLocationDetailType.text = location.type
        binding.textLocationDetailDimension.text = location.dimension
    }

    private fun setting() {
        binding.rvLocationResidents.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = locationResidentsAdapter
        }
        binding.ivLocationDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        location.residents?.let {
            LocationResidentsList.locationResidentsList.clear()
            LocationResidentsList.locationResidentsList.addAll(it)
        }
        locationResidentsAdapter.refresh()
    }

    private fun getLocationFromArguments() {
        arguments?.let { it ->
            it.getSerializable(KEY_LOCATION)?.let {
                if (it is LocationDatabaseEntity) {
                    location = it
                } else {
                    error(getString(R.string.error_init_fragment_location_details))
                }
            }
        } ?: error(getString(R.string.error_init_fragment_location_details))
    }

    companion object {
        private const val KEY_LOCATION = "KEY_LOCATION"
        fun newInstance(location: LocationDatabaseEntity?): FragmentLocationDetails {
            val bundle = bundleOf(KEY_LOCATION to location)
            return FragmentLocationDetails().apply { arguments = bundle }
        }
    }

}