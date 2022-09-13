package com.sidukov.sparvel.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sidukov.sparvel.core.functionality.BaseViewModel
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel() {

    var uiState by mutableStateOf(HomeScreenState())
        private set

}

data class HomeScreenState(
    val trackList: List<Track> = emptyList(),
    val albums: List<MusicCollection> = emptyList()
)