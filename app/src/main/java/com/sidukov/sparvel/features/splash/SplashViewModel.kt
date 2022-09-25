package com.sidukov.sparvel.features.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidukov.sparvel.core.functionality.MusicDataProvider
import com.sidukov.sparvel.core.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val musicDataProvider: MusicDataProvider
) : ViewModel() {

    var uiState by mutableStateOf(SplashScreenState())
        private set

    fun setAnimationVisible() {
        uiState = uiState.copy(isAnimationVisible = true)
    }

    suspend fun readTracks() {
        uiState = uiState.copy(
            trackList = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                musicDataProvider.getAllDeviceTracks()
            },
            isDataLoaded = true
        )
    }
}

data class SplashScreenState(
    val trackList: List<Track> = emptyList(),
    val isAnimationVisible: Boolean = false,
    val isDataLoaded: Boolean = false
)