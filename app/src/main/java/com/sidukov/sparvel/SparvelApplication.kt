package com.sidukov.sparvel

import android.app.Application
import com.sidukov.sparvel.core.functionality.storage.SharedPrefsManager
import com.sidukov.sparvel.di.AppComponent
import com.sidukov.sparvel.di.AppModule
import com.sidukov.sparvel.di.DaggerAppComponent
import com.sidukov.sparvel.di.ResourceModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SparvelApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    var instance: SparvelApplication? = null

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var preferences: SharedPrefsManager

        init {
            System.loadLibrary("sparvel")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .resourceModule(ResourceModule())
            .build()
        preferences = SharedPrefsManager(this)
    }
}