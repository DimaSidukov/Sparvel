package com.sidukov.sparvel.di

import android.content.Context
import com.sidukov.audiomanager.AudioManager
import com.sidukov.sparvel.core.functionality.service.MusicDataProvider
import com.sidukov.sparvel.core.functionality.storage.SharedPrefsManager
import com.sidukov.sparvel.core.functionality.storage.StorageManager

class Injector(context: Context) {

    val musicDataProvider = MusicDataProvider(context)

    val storageManager = StorageManager(
        SharedPrefsManager(context)
    )

    val audioManager = AudioManager(context)

}