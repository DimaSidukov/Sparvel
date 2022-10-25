package com.sidukov.sparvel.di

import android.content.Context
import com.sidukov.sparvel.core.functionality.storage.SharedPrefsManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideSharedPrefsManager(context: Context): SharedPrefsManager =
        SharedPrefsManager(context)

}