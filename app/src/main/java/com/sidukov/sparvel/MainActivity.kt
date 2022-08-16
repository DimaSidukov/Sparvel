package com.sidukov.sparvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sidukov.sparvel.core.functionality.ScreenNavigation
import com.sidukov.sparvel.core.theme.SparvelTheme

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
                    ScreenNavigation(navController)
                }
            }
        }
    }
}