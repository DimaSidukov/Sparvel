package com.sidukov.sparvel.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidukov.sparvel.core.functionality.providers.MusicDataProvider
import com.sidukov.sparvel.core.functionality.storage.StorageManager
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val musicDataProvider: MusicDataProvider,
    private val storageManager: StorageManager
) : ViewModel() {

    var uiState by mutableStateOf(HomeScreenState())
        private set

    fun setScreen(screen: HomeScreen) {
        uiState = uiState.copy(currentScreen = screen)
    }

    fun showPlayer(track: Track) {
        if (track != uiState.selectedTrack) {
            uiState = uiState.copy(selectedTrack = track)
        }

        storageManager.settings.trackId = track.id
    }

    suspend fun readTracks(): List<Track> {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            musicDataProvider.getAllDeviceTracks().also {
                uiState = uiState.copy(
                    selectedTrack = it.find { track -> track.id == storageManager.settings.trackId }
                )
            }
        }
    }

}

data class HomeScreenState(
    val trackList: List<Track> = emptyList(),
    val albums: List<MusicCollection> = emptyList(),
    val currentScreen: HomeScreen = HomeScreen.FULL,
    val selectedTrack: Track? = null
)

sealed class HomeScreen {
    object FULL : HomeScreen()
    object PLAYLISTS : HomeScreen()
    object ALBUMS : HomeScreen()
    object LIBRARY : HomeScreen()
}