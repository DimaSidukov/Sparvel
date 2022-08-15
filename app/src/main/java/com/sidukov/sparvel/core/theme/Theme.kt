package com.sidukov.sparvel.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
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

@Composable
fun SparvelTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    val typography = SparvelTypography()
    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colorScheme = colors.material,
            typography = typography.material,
            content = content
        )
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(SparvelTheme.colors.background)
    systemUiController.setStatusBarColor(SparvelTheme.colors.background)
    systemUiController.setNavigationBarColor(SparvelTheme.colors.background)
}