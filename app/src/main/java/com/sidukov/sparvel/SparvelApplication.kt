package com.sidukov.sparvel

import android.app.Application
import com.sidukov.sparvel.core.functionality.SharedPrefsManager
import com.sidukov.sparvel.di.AppComponent
import com.sidukov.sparvel.di.DaggerAppComponent
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
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder().build()
        preferences = SharedPrefsManager(this)
    }
}