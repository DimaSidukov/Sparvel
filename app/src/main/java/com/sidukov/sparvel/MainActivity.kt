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
                    // add this code snippet to change app theme on button click. save state in
                    // datastore/shared preferences
                    // https://stackoverflow.com/questions/65192409/jetpack-compose-how-to-change-theme-from-light-to-dark-mode-programmatically-on
                    ScreenNavigation(navController)
                }
            }
        }
    }
}