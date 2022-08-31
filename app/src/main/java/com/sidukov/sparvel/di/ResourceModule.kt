package com.sidukov.sparvel.di

import android.content.Context
import com.sidukov.sparvel.core.functionality.MusicDataProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResourceModule {

    @Singleton
    @Provides
    fun provideMusicDataProvider(context: Context): MusicDataProvider = MusicDataProvider(context)

}