package com.sidukov.sparvel.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
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

object RippleEffect : RippleTheme {
    @Composable
    override fun defaultColor(): Color = SparvelTheme.colors.rippleColor

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        SparvelTheme.colors.rippleColor,
        lightTheme = !isSystemInDarkTheme()
    )
}

@Composable
fun SparvelTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = (if (darkTheme) DarkColors else LightColors)
    val typography = SparvelTypography()

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent)
    }

    MaterialTheme(
        colorScheme = colors.material,
        typography = typography.material
    ) {
        CompositionLocalProvider(
            LocalColors provides colors,
            LocalRippleTheme provides RippleEffect,
            content = content
        )
    }
}