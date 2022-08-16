package com.sidukov.sparvel.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val black = Color.Black
private val white = Color.White
private val lightBlue = Color(0xFF748CAB)
private val darkestBlue = Color(0xFF00081C)
private val lightGrey = Color(0xFFBEC4CC)
private val lightGrey2 = Color(0xFFE6E6E6)
private val darkGrey = Color(0xFF5F5F5F)

data class SparvelColors(
    val material: ColorScheme,
    val text: Color,
    val textPlaceholder: Color,
    val searchBorder: Color,
    val drawerText: Color,
    private val secondaryMaterial: Color,
    private val backgroundMaterial: Color
) {
    val primary: Color get() = material.primary
    val secondary: Color get() = secondaryMaterial
    val background: Color get() = backgroundMaterial
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
    searchBorder = lightGrey,
    drawerText = white,
    secondaryMaterial = black,
    backgroundMaterial = white
)

val DarkColors = SparvelColors(
    material = darkColorScheme(),
    text = white,
    textPlaceholder = darkGrey,
    searchBorder = lightGrey2,
    drawerText = white,
    secondaryMaterial = white,
    backgroundMaterial = darkestBlue
)