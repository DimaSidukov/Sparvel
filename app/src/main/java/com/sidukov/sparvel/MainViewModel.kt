package com.sidukov.sparvel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sidukov.sparvel.core.functionality.storage.AppTheme
import com.sidukov.sparvel.core.functionality.storage.StorageManager

class MainViewModel(private val storageManager: StorageManager) : ViewModel() {

    var appTheme by mutableStateOf(storageManager.settings.appTheme)

    fun switchAppTheme() {
        appTheme =
            if (appTheme == AppTheme.LIGHT.code) AppTheme.DARK.code else AppTheme.LIGHT.code
        storageManager.settings.appTheme = appTheme
    }

}