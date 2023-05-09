package com.sidukov.sparvel.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidukov.audiomanager.AudioManager
import com.sidukov.sparvel.core.functionality.service.MusicDataProvider
import com.sidukov.sparvel.core.functionality.storage.StorageManager
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.features.home.PlayerState.Paused
import com.sidukov.sparvel.features.home.PlayerState.Playing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val musicDataProvider: MusicDataProvider,
    private val storageManager: StorageManager,
    private val audioManager: AudioManager
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

    fun updatePlayerState() {
        uiState = if (uiState.playerState == Playing) {
            uiState.copy(playerState = Paused)
        } else {
            uiState.selectedTrack?.let(::playTrack)
            uiState.copy(playerState = Playing)
        }
    }

    private fun playTrack(track: Track) {
        audioManager.play(track.fullPath)
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
    val selectedTrack: Track? = null,
    val playerState: PlayerState = Paused
)

sealed class HomeScreen {
    object FULL : HomeScreen()
    object PLAYLISTS : HomeScreen()
    object ALBUMS : HomeScreen()
    object LIBRARY : HomeScreen()
}

sealed class PlayerState {
    object Playing : PlayerState()
    object Paused : PlayerState()
}