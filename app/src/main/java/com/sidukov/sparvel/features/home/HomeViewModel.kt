package com.sidukov.sparvel.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sidukov.sparvel.core.functionality.MusicDataProvider
import com.sidukov.sparvel.core.model.TrackItem
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val musicDataProvider: MusicDataProvider
) : ViewModel() {

    var uiState by mutableStateOf(HomeScreenState())
        private set

    init {
        readTracks()
    }

    private fun readTracks() {
        uiState = uiState.copy(
            trackList = musicDataProvider.getAllDeviceTracks()
        )
    }
}

data class HomeScreenState(
    val trackList: List<TrackItem> = emptyList()
)