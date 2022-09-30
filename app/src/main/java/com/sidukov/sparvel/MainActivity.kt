package com.sidukov.sparvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.sidukov.sparvel.core.functionality.AppTheme
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.di.ViewModelFactory
import com.sidukov.sparvel.features.main.MainContainerScreen
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        SparvelApplication.appComponent.inject(this)
        setContent {

            val navController = rememberNavController()
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)

            var theme by remember { mutableStateOf(SparvelApplication.preferences.appTheme) }

            val useDarkColors = when (theme) {
                AppTheme.DARK.code -> true
                AppTheme.LIGHT.code -> false
                else -> true
            }

            SparvelTheme(darkTheme = useDarkColors) {
                Surface(
                    modifier = Modifier
                        .background(
                            SparvelTheme.colors.backgroundGradient ?: SparvelTheme.colors.background
                        ),
                    color = Color.Transparent
                ) {
                    MainContainerScreen(navController, viewModelProvider) {
                        SparvelApplication.preferences.appTheme =
                            if (theme == AppTheme.LIGHT.code) AppTheme.DARK.code else AppTheme.LIGHT.code
                        theme =
                            if (theme == AppTheme.LIGHT.code) AppTheme.DARK.code else AppTheme.LIGHT.code
                    }
                }
            }
        }
    }
}
