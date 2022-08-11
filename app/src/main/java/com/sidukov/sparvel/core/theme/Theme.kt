package com.sidukov.sparvel.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

object SparvelTheme {
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

internal val LocalColors = staticCompositionLocalOf { lightColors }
internal val LocalTypography = staticCompositionLocalOf { SparvelTypography }

@Composable
fun SparvelTheme(
    colors: ColorScheme = SparvelTheme.colors,
    typography: Typography = SparvelTheme.typography,
    content: @Composable () -> Unit
) = MaterialTheme(colorScheme = colors, typography = typography) {
    content()
}