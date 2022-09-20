package com.sidukov.sparvel.features.splash

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

    suspend fun readTracks(): List<Track> {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            musicDataProvider.getAllDeviceTracks()
        }
    }

}