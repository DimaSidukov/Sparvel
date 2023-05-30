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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val musicDataProvider: MusicDataProvider,
    private val storageManager: StorageManager,
    private val audioManager: AudioManager
) : ViewModel() {


    private var lastUpdatedPosition = 0f

    var uiState by mutableStateOf(HomeScreenState())
        private set

    val playbackPosition = MutableSharedFlow<Float>().apply flow@{
        viewModelScope.launch {
            audioManager.currentPosition.collect { position ->
                this@flow.emit(
                    uiState.selectedTrack?.duration?.let { duration ->
                        position.toFloat() / duration.toFloat()
                    } ?: 0f
                )
            }
        }
    }

    val playbackTimestamp = playbackPosition
        .map {
            uiState.selectedTrack?.duration?.let { duration ->
                (it * duration).toLong()
            } ?: 0L
        }
        .shareIn(viewModelScope, SharingStarted.Lazily)

    private var currentlyPlayingTrack: Track? = null

    fun setScreen(screen: HomeScreen) {
        uiState = uiState.copy(currentScreen = screen)
    }

    fun showPlayer(track: Track) {
        if (track != uiState.selectedTrack) {
            uiState = uiState.copy(selectedTrack = track)
        }
        storageManager.settings.trackId = track.id
        uiState = uiState.copy(playerState = Playing)
        finish()
        play(track)
    }

    fun updatePlayerState() {
        uiState.selectedTrack?.let {
            uiState = uiState.copy(
                playerState = if (uiState.playerState == Playing) Paused else Playing
            )
            play(it)
        }
    }

    private fun play(track: Track) {
        if (currentlyPlayingTrack != track) {
            // finish()
            audioManager.play(track.fullPath)
            currentlyPlayingTrack = track
        } else pause()
    }

    private fun pause() {
        audioManager.pause()
    }

    private var newValue: Float = 0f

    fun onPlayerPositionUpdated(value: Float) {
        viewModelScope.launch {
            audioManager.onSeekStarted()
            newValue = value
            playbackPosition.emit(value)
        }
        lastUpdatedPosition = value
    }

    fun seek() {
        uiState.selectedTrack?.duration?.let { duration ->
            audioManager.seek(
                (lastUpdatedPosition * duration).toLong()
            )
        }
    }

    fun finish() {
        audioManager.finish()
    }

    fun onRepeatClicked() {

    }

    fun onPreviousClicked() {

    }

    fun onNextClicked() {

    }

    fun onCurrentPlaylistClicked() {

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
    val playerState: PlayerState = Paused,
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