package com.example.finalprojectrickandmorty.ui.characterUi

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.adapter.characterAdapter.CharacterEpisodeAdapter
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.databinding.FragmentCharacterDetailsBinding
import com.example.finalprojectrickandmorty.ui.episodeUi.FragmentEpisodeMain
import com.example.finalprojectrickandmorty.ui.locationUi.FragmentLocationMain
import com.example.finalprojectrickandmorty.util.CharacterEpisodesList
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_CHARACTER_DETAILS
import com.example.finalprojectrickandmorty.viewModel.episodeViewModel.EpisodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentCharacterDetails : Fragment(R.layout.fragment_character_details) {

    private lateinit var binding: FragmentCharacterDetailsBinding
    private lateinit var character: CharacterDatabaseEntity

    private val characterDetailViewModel: EpisodeViewModel by viewModels()

    /*
    До лучших времен
    private val originViewModel: OriginViewModel by viewModels()
     */

    private lateinit var itemEpisodeOnClickListener: FragmentEpisodeMain.ItemEpisodeOnClickListener
    private lateinit var itemOriginOnClickListener: FragmentLocationMain.ItemLocationOnClickListener

    private val characterEpisodeAdapter by lazy {
        CharacterEpisodeAdapter { episodeDatabaseEntity ->
            itemEpisodeOnClickListener.onItemEpisodeClicked(
                episodeDatabaseEntity, KEY_FRAGMENT_CHARACTER_DETAILS
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentEpisodeMain.ItemEpisodeOnClickListener) itemEpisodeOnClickListener =
            context
        if (context is FragmentLocationMain.ItemLocationOnClickListener) itemOriginOnClickListener =
            context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharacterDetailsBinding.bind(view)
        getCharacterFromArguments()
        bindViews(view)
        setting()
        lifecycleScope.launch {
            characterDetailViewModel.dataSource.collectLatest {
                characterEpisodeAdapter.submitData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        characterEpisodeAdapter.refresh()
    }

    private fun bindViews(v: View) {
        binding.textCharacterDetailName.text = character.name
        binding.textCharacterDetailSpecies.text = character.species
        binding.textCharacterDetailStatus.text = character.status
        binding.textCharacterDetailGender.text = character.gender
        binding.textCharacterDetailType.text = character.type
        binding.textCharacterDetailOrigin.text = character.origin?.name
        binding.textCharacterDetailLocation.text = character.location?.name
        Glide.with(v).load(character.image).into(binding.imageCharacterDetail)
    }

    private fun setting() {
        binding.rvCharactersEpisodes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = characterEpisodeAdapter
        }
        binding.ivCharacterDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        character.episode?.let {
            CharacterEpisodesList.characterEpisodesList.clear()
            CharacterEpisodesList.characterEpisodesList.addAll(it)
        }

        /*
        Этот блок оставляю на будущее

        originViewModel.singleLocation.observe(viewLifecycleOwner) {
            originViewModel.singleLocation.value?.let { location ->
                itemOriginOnClickListener.onItemLocationClicked(location, KEY_FRAGMENT_CHARACTER_DETAILS)
            }
        }

        binding.textCharacterDetailOrigin.setOnClickListener {
            Log.i("TAG", "Fragment Details ORIGIN onClicked!")
            CharacterOriginAndLocationList.originAndLocation = character.origin?.url.toString()
            lifecycleScope.launch {
                originViewModel.singleLocation()
            }
        }
        */
    }

    private fun getCharacterFromArguments() {
        arguments?.let { it ->
            it.getSerializable(KEY_CHARACTER)?.let {
                if (it is CharacterDatabaseEntity) {
                    character = it
                } else {
                    error(getString(R.string.error_init_fragment_character_details))
                }
            }
        } ?: error(getString(R.string.error_init_fragment_character_details))
    }

    companion object {
        private const val KEY_CHARACTER = "KEY_CHARACTER"
        fun newInstance(character: CharacterDatabaseEntity?): FragmentCharacterDetails {
            val bundle = bundleOf(KEY_CHARACTER to character)
            return FragmentCharacterDetails().apply { arguments = bundle }
        }
    }

}