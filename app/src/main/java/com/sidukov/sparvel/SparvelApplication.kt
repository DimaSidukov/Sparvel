package com.sidukov.sparvel

import android.app.Application
import android.content.Context
import com.sidukov.sparvel.di.Injector

class SparvelApplication : Application() {

    companion object {
        private var injector: Injector? = null
        private fun init(context: Context) {
            injector = Injector(context)
        }

        fun getInjector() = injector!!
    }

    override fun onCreate() {
        super.onCreate()
        init(this)
    }
}