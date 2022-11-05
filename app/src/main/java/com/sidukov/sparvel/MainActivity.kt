package com.sidukov.sparvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.functionality.storage.AppTheme
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.di.ViewModelFactory
import com.sidukov.sparvel.features.main.MainContainerScreen
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        SparvelApplication.appComponent.inject(this)

        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider[MainViewModel::class.java]

        setContent {

            val navController = rememberNavController()
            val isDarkTheme by remember {
                derivedStateOf {
                    viewModel.appTheme == AppTheme.DARK.code
                }
            }

            SparvelTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier
                        .background(
                            SparvelTheme.colors.backgroundGradient ?: SparvelTheme.colors.background
                        ),
                    color = Color.Transparent
                ) {
                    MainContainerScreen(
                        navController = navController,
                        viewModelProvider = viewModelProvider,
                        onAppThemeChanged = {
                            viewModel.switchAppTheme()
                        }
                    )
                }
            }
        }
    }
}
