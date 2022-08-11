package com.sidukov.sparvel.core.theme

import androidx.compose.runtime.*

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

internal val LocalColors = staticCompositionLocalOf { lightColors() }
internal val LocalTypography = staticCompositionLocalOf { SparvelTypography() }

@Composable
fun SparvelTheme(
    colors: SparvelColors = SparvelTheme.colors,
    typography: SparvelTypography = SparvelTheme.typography,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalTypography provides typography
    ) {
        content()
    }
}