package com.sidukov.sparvel.di

import android.content.Context
import com.sidukov.sparvel.core.functionality.service.AudioManager
import com.sidukov.sparvel.core.functionality.storage.SharedPrefsManager
import com.sidukov.sparvel.core.functionality.service.MusicDataProvider
import com.sidukov.sparvel.core.functionality.storage.StorageManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResourceModule {

    @Singleton
    @Provides
    fun provideMusicDataProvider(context: Context): MusicDataProvider = MusicDataProvider(context)

    @Singleton
    @Provides
    fun provideStorageManager(settings: SharedPrefsManager) = StorageManager(settings)

    @Singleton
    @Provides
    fun provideAudioManager() = AudioManager()

}