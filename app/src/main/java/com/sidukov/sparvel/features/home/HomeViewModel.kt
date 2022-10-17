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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val musicDataProvider: MusicDataProvider
) : ViewModel() {

    var uiState by mutableStateOf(HomeScreenState())
        private set

    fun setScreen(screen: HomeScreen) {
        uiState = uiState.copy(currentScreen = screen)
    }

    fun setScreenAndDisableAnimation(screen: HomeScreen) {
        uiState = uiState.copy(
            currentScreen = screen,
            isNewScreenVisible = false
        )
    }

    fun showNewScreen() {
        uiState = uiState.copy(isNewScreenVisible = true)
    }

    fun showPlayer(track: Track) {
        if (track != uiState.selectedTrack) {
            uiState = uiState.copy(selectedTrack = track)
        }
    }

    suspend fun readTracks(): List<Track> {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            musicDataProvider.getAllDeviceTracks()
        }
    }

}

data class HomeScreenState(
    val trackList: List<Track> = emptyList(),
    val albums: List<MusicCollection> = emptyList(),
    val currentScreen: HomeScreen = HomeScreen.FULL,
    val isNewScreenVisible: Boolean = false,
    val selectedTrack: Track? = null
)

sealed class HomeScreen {
    object FULL : HomeScreen()
    object PLAYLISTS : HomeScreen()
    object ALBUMS : HomeScreen()
    object LIBRARY : HomeScreen()
}