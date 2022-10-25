package com.sidukov.sparvel.di

import android.content.Context
import com.sidukov.sparvel.core.functionality.storage.SharedPrefsManager
import com.sidukov.sparvel.core.functionality.providers.MusicDataProvider
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

}