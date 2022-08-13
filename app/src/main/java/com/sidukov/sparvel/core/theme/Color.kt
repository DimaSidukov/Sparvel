package com.sidukov.sparvel.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val black = Color.Black
val white = Color.White
val lightBlue = Color(0xFF748CAB)
val darkestBlue = Color(0xFF00081C)
val lightGrey = Color(0xFFBEC4CC)
val lightGrey2 = Color(0xFFE6E6E6)
val darkGrey = Color(0xFF5F5F5F)

data class SparvelColors(
    val material: ColorScheme,
    val text: Color,
    val textPlaceholder: Color,
    val searchBorder: Color
) {
    val primary: Color get() = material.primary
    val secondary: Color get() = material.secondary
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
}

val LightColors = SparvelColors(
    material = lightColorScheme(),
    text = black,
    textPlaceholder = lightBlue,
    searchBorder = lightGrey
)

val DarkColors = SparvelColors(
    material = darkColorScheme(),
    text = white,
    textPlaceholder = darkGrey,
    searchBorder = lightGrey2
)