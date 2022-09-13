package com.sidukov.sparvel.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val black = Color.Black
private val white = Color.White
private val lightBlue = Color(0xFF748CAB)
private val darkestBlue = Color(0xFF00081C)
private val lightGrey = Color(0xFFBEC4CC)
private val lightGrey2 = Color(0xFF999999)
private val darkGrey = Color(0xFF5F5F5F)
private val gloomyBlue = Color(0xFF01232E)
private val dimGrey = Color(0xFFE3E3E3)

data class SparvelColors(
    val material: ColorScheme,
    val navigation: Color,
    val text: Color,
    val textPlaceholder: Color,
    val searchText: Color,
    val searchBorder: Color,
    val cursor: Color,
    val drawer: Color,
    val drawerText: Color,
    val backgroundGradient: Brush?,
    val logoGradient: Brush,
    val permissionDenied: Color,
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
    navigation = black,
    text = black,
    textPlaceholder = lightBlue,
    searchText = lightBlue,
    searchBorder = lightGrey,
    cursor = black,
    drawer = lightBlue,
    drawerText = white,
    backgroundGradient = null,
    logoGradient = Brush.verticalGradient(listOf(lightBlue, black)),
    permissionDenied = darkGrey,
    secondaryMaterial = black,
    backgroundMaterial = white
)

val DarkColors = SparvelColors(
    material = darkColorScheme(),
    navigation = dimGrey,
    text = white,
    textPlaceholder = darkGrey,
    searchText = lightGrey2,
    searchBorder = lightGrey2,
    cursor = white,
    drawer = darkestBlue,
    drawerText = white,
    backgroundGradient = Brush.verticalGradient(listOf(darkestBlue, gloomyBlue)),
    logoGradient = Brush.verticalGradient(listOf(darkestBlue, white)),
    permissionDenied = lightGrey2,
    secondaryMaterial = white,
    backgroundMaterial = darkestBlue
)