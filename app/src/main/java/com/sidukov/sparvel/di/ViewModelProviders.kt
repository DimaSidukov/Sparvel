package com.sidukov.sparvel.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidukov.sparvel.MainViewModel
import com.sidukov.sparvel.SparvelApplication
import com.sidukov.sparvel.features.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> constructViewModel(): ViewModelProvider.NewInstanceFactory =
    object : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (VM::class) {
                MainViewModel::class -> MainViewModel(
                    SparvelApplication.getInjector().storageManager
                )
                HomeViewModel::class -> HomeViewModel(
                    SparvelApplication.getInjector().musicDataProvider,
                    SparvelApplication.getInjector().storageManager,
                    SparvelApplication.getInjector().audioManager
                )
                else -> throw ClassNotFoundException()
            } as T
        }
    }

@Composable
inline fun <reified VM : ViewModel> viewModelWithFactory() =
    androidx.lifecycle.viewmodel.compose.viewModel<VM>(
        factory = constructViewModel<VM>()
    )