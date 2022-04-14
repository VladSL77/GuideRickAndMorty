package com.example.finalprojectrickandmorty.ui.characterUi

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.util.CharactersSearchParams
import com.example.finalprojectrickandmorty.viewModel.characterViewModel.CharacterSearchViewModel


class FragmentCharacterSearch : DialogFragment(R.layout.fragment_character_search) {

    private lateinit var ivSearchBack: ImageView
    private lateinit var etName: EditText
    private lateinit var etStatus: EditText
    private lateinit var etSpecies: EditText
    private lateinit var etType: EditText
    private lateinit var etGender: EditText
    private lateinit var buttonRunSearch: Button

    private val characterSearchViewModel: CharacterSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setting()
    }

    private fun init() {
        ivSearchBack = requireView().findViewById(R.id.iv_character_detail_back)
        etName = requireView().findViewById(R.id.et_character_name)
        etStatus = requireView().findViewById(R.id.et_character_status)
        etSpecies = requireView().findViewById(R.id.et_character_species)
        etType = requireView().findViewById(R.id.et_character_type)
        etGender = requireView().findViewById(R.id.et_character_gender)
        buttonRunSearch = requireView().findViewById(R.id.button_character_run_search)
    }

    private fun setting() {
        ivSearchBack.setOnClickListener {
            dismiss()
        }
        buttonRunSearch.setOnClickListener {
            createSearchParams()
            characterSearchViewModel.selectItem(getString(R.string.start_search))
            dismiss()
        }
    }

    private fun createSearchParams() {
        CharactersSearchParams.name = etName.text.toString()
        CharactersSearchParams.status = etStatus.text.toString()
        CharactersSearchParams.species = etSpecies.text.toString()
        CharactersSearchParams.type = etType.text.toString()
        CharactersSearchParams.gender = etGender.text.toString()
    }

}