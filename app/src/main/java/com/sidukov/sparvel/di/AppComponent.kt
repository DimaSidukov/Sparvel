package com.sidukov.sparvel.di

import com.sidukov.sparvel.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        ResourceModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
}
