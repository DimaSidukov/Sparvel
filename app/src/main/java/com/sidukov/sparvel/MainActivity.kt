package com.sidukov.sparvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
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
        setContent {

            val navController = rememberNavController()
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)

            SparvelTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            SparvelTheme.colors.backgroundGradient ?: SparvelTheme.colors.background
                        ),
                    color = Color.Transparent
                ) {
                    MainContainerScreen(navController, viewModelProvider)
                }
            }
        }
    }
}
