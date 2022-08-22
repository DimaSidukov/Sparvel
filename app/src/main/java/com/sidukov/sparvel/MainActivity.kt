package com.sidukov.sparvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.features.main.MainContainerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            SparvelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SparvelTheme.colors.background
                ) {
                    MainContainerScreen(navController)
                }
            }
        }
    }
}