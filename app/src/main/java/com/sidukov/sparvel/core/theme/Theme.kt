package com.sidukov.sparvel.core.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sidukov.sparvel.SparvelApplication
import com.sidukov.sparvel.core.functionality.storage.AppTheme

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

private object Ripple : RippleTheme {
    @Composable
    override fun defaultColor(): Color = RippleTheme.defaultRippleColor(
        Color.Red,
        lightTheme = !isSystemInDarkTheme()
    )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.Red,
        lightTheme = !isSystemInDarkTheme()
    )
}

@Composable
fun SparvelTheme(
    darkTheme: Boolean = SparvelApplication.preferences.appTheme == AppTheme.DARK.code,
    content: @Composable () -> Unit
) {
    val colors = (if (darkTheme) DarkColors else LightColors)

    val typography = SparvelTypography()

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent)
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalRippleTheme provides Ripple
    ) {
        MaterialTheme(
            colorScheme = colors.material,
            typography = typography.material,
            content = content
        )
    }
}