package com.sidukov.sparvel.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidukov.sparvel.core.functionality.MusicDataProvider
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        viewModelScope.launch(Dispatchers.IO) {
            musicDataProvider.getAllDeviceTracks().collect {
                viewModelScope.launch(Dispatchers.Main) {
                    uiState = uiState.copy(
                        trackList = it.first,
                        albums = it.second
                    )
                }
            }
        }
    }
}

data class HomeScreenState(
    val trackList: List<Track> = emptyList(),
    val albums: List<MusicCollection> = emptyList()
)