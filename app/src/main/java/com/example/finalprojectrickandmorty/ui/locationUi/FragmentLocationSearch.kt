package com.example.finalprojectrickandmorty.ui.locationUi

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.util.LocationSearchParams
import com.example.finalprojectrickandmorty.viewModel.locationViewModel.LocationSearchViewModel


class FragmentLocationSearch : DialogFragment(R.layout.fragment_location_search) {


    private lateinit var ivSearchBack: ImageView
    private lateinit var etName: EditText
    private lateinit var etType: EditText
    private lateinit var etDimension: EditText
    private lateinit var buttonRunSearch: Button

    private val locationSearchViewModel: LocationSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setting()
    }

    private fun init() {
        ivSearchBack = requireView().findViewById(R.id.iv_location_detail_back)
        etName = requireView().findViewById(R.id.et_location_name)
        etType = requireView().findViewById(R.id.et_location_type)
        etDimension = requireView().findViewById(R.id.et_location_dimension)
        buttonRunSearch = requireView().findViewById(R.id.button_location_run_search)
    }

    private fun setting() {
        ivSearchBack.setOnClickListener {
            dismiss()
        }
        buttonRunSearch.setOnClickListener {
            createSearchParams()
            locationSearchViewModel.selectItem(getString(R.string.start_search))
            dismiss()
        }
    }

    private fun createSearchParams() {
        LocationSearchParams.name = etName.text.toString()
        LocationSearchParams.type = etType.text.toString()
        LocationSearchParams.dimension = etDimension.text.toString()
    }

}