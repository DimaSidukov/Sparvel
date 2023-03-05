@file:Suppress("UNCHECKED_CAST")

package com.sidukov.sparvel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidukov.sparvel.MainViewModel
import com.sidukov.sparvel.core.functionality.service.AudioManager
import com.sidukov.sparvel.core.functionality.service.MusicDataProvider
import com.sidukov.sparvel.core.functionality.storage.StorageManager
import com.sidukov.sparvel.features.home.HomeViewModel

class MainViewModelFactory(private val storageManager: StorageManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(storageManager) as T
}

class HomeViewModelFactory(
    private val musicDataProvider: MusicDataProvider,
    private val storageManager: StorageManager,
    private val audioManager: AudioManager
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        HomeViewModel(musicDataProvider, storageManager, audioManager) as T
}