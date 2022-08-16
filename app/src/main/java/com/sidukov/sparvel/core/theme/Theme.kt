package com.sidukov.sparvel.core.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object SparvelTheme {
    val colors: SparvelColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: SparvelTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

internal val LocalColors = staticCompositionLocalOf { LightColors }
internal val LocalTypography = staticCompositionLocalOf { SparvelTypography() }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SparvelTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    val typography = SparvelTypography()

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(colors.background)
    systemUiController.setStatusBarColor(colors.background)
    systemUiController.setNavigationBarColor(colors.background)

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalOverscrollConfiguration provides null
    ) {
        MaterialTheme(
            colorScheme = colors.material,
            typography = typography.material,
            content = content
        )
    }
}