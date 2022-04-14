package com.example.finalprojectrickandmorty.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.finalprojectrickandmorty.R
import com.example.finalprojectrickandmorty.database.characterDB.CharacterDatabaseEntity
import com.example.finalprojectrickandmorty.database.episodeDB.EpisodeDatabaseEntity
import com.example.finalprojectrickandmorty.database.locationDB.LocationDatabaseEntity
import com.example.finalprojectrickandmorty.ui.characterUi.FragmentCharacterDetails
import com.example.finalprojectrickandmorty.ui.characterUi.FragmentCharacterMain
import com.example.finalprojectrickandmorty.ui.episodeUi.FragmentEpisodeDetails
import com.example.finalprojectrickandmorty.ui.episodeUi.FragmentEpisodeMain
import com.example.finalprojectrickandmorty.ui.locationUi.FragmentLocationDetails
import com.example.finalprojectrickandmorty.ui.locationUi.FragmentLocationMain
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_CHARACTER_MAIN
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_EPISODE_MAIN
import com.example.finalprojectrickandmorty.util.Constants.KEY_FRAGMENT_LOCATION_MAIN
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator,
    FragmentCharacterMain.ItemCharacterOnClickListener,
    FragmentEpisodeMain.ItemEpisodeOnClickListener,
    FragmentLocationMain.ItemLocationOnClickListener {

    private lateinit var tabCharacter: TextView
    private lateinit var tabLocations: TextView
    private lateinit var tabEpisodes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setting()
        navigateFragmentContainer(FragmentCharacterMain(), KEY_FRAGMENT_CHARACTER_MAIN)
    }

    private fun init() {
        tabCharacter = findViewById(R.id.tab_characters)
        tabLocations = findViewById(R.id.tab_locations)
        tabEpisodes = findViewById(R.id.tab_episodes)
    }

    private fun setting() {
        tabCharacter.setOnClickListener {
            navigateFragmentContainer(FragmentCharacterMain(), KEY_FRAGMENT_CHARACTER_MAIN)
            setColorForTab(tabCharacter, tabLocations, tabEpisodes)
        }
        tabLocations.setOnClickListener {
            navigateFragmentContainer(FragmentLocationMain(), KEY_FRAGMENT_LOCATION_MAIN)
            setColorForTab(tabLocations, tabCharacter, tabEpisodes)
        }
        tabEpisodes.setOnClickListener {
            navigateFragmentContainer(FragmentEpisodeMain(), KEY_FRAGMENT_EPISODE_MAIN)
            setColorForTab(tabEpisodes, tabCharacter, tabLocations)
        }
    }

    private fun setColorForTab(focus: TextView, firstBack: TextView, secondBack: TextView) {
        focus.setBackgroundColor(resources.getColor(R.color.main_grey))
        firstBack.setBackgroundColor(resources.getColor(R.color.second_gray))
        secondBack.setBackgroundColor(resources.getColor(R.color.second_gray))
    }

    override fun navigateFragmentContainer(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(tag)
            .commit()
    }

    override fun onItemCharacterClicked(character: CharacterDatabaseEntity, tag: String) {
        navigateFragmentContainer(FragmentCharacterDetails.newInstance(character), tag)
    }

    override fun onItemEpisodeClicked(episode: EpisodeDatabaseEntity, tag: String) {
        navigateFragmentContainer(FragmentEpisodeDetails.newInstance(episode), tag)
    }

    override fun onItemLocationClicked(location: LocationDatabaseEntity, tag: String) {
        navigateFragmentContainer(FragmentLocationDetails.newInstance(location), tag)
    }

    private var clickCountForExit = 0
    override fun onBackPressed() {
        super.onBackPressed()
        if (clickCountForExit == 2) {
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
        this.clickCountForExit++
        Toast.makeText(this, getString(R.string.confirm_exit), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ clickCountForExit = 0 }, 3000)
    }

}