package com.example.finalprojectrickandmorty.ui

import androidx.fragment.app.Fragment

interface Navigator {

    fun navigateFragmentContainer(fragment: Fragment, tag: String)

}