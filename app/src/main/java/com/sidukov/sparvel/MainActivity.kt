package com.sidukov.sparvel

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.applyCanvas
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.sidukov.sparvel.core.functionality.AppTheme
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.di.ViewModelFactory
import com.sidukov.sparvel.features.main.MainContainerScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SparvelApplication.appComponent.inject(this)
        setContent {

            val navController = rememberNavController()
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)

            var theme by remember { mutableStateOf(SparvelApplication.preferences.appTheme) }
            val useDarkColors = when (theme) {
                AppTheme.DARK.code -> {
//                    val view = LocalView.current
//                    val context = LocalContext.current
//
//                    val handler = Handler(Looper.getMainLooper())
//                    handler.postDelayed(Runnable {
//                        val bmp = Bitmap.createBitmap(view.width, view.height,
//                            Bitmap.Config.ARGB_8888).applyCanvas {
//                            view.draw(this)
//                        }
//                        bmp.let {
//                            File(context.filesDir, "sparvel_screenshot.png")
//                                .writeBitmap(bmp, Bitmap.CompressFormat.PNG, 85)
//                        }
//                    }, 1000)
                    true
                }
                AppTheme.LIGHT.code -> false
                else -> true
            }

            SparvelTheme(darkTheme = useDarkColors) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            SparvelTheme.colors.backgroundGradient ?: SparvelTheme.colors.background
                        ),
                    color = Color.Transparent
                ) {
                    MainContainerScreen(navController, viewModelProvider) {
                        SparvelApplication.preferences.appTheme =
                            if (theme == AppTheme.LIGHT.code) AppTheme.DARK.code else AppTheme.LIGHT.code
                        theme = if (theme == AppTheme.LIGHT.code) AppTheme.DARK.code else AppTheme.LIGHT.code
                    }
                }
            }
        }
    }

//    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
//        outputStream().use { out ->
//            bitmap.compress(format, quality, out)
//            out.flush()
//        }
//    }
}
