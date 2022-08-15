package com.sidukov.sparvel.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.sidukov.sparvel.R

val LatoFontFamily = FontFamily(
    Font(R.font.lato_black, FontWeight.Black),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_semibold, FontWeight.SemiBold),
    Font(R.font.lato_medium, FontWeight.Medium),
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_thin, FontWeight.Thin)
)

internal fun TextStyle(weight: FontWeight, size: TextUnit = 16.sp) = TextStyle(
    fontFamily = LatoFontFamily,
    fontWeight = weight,
    fontSize = size
)

data class SparvelTypography(
    val material: Typography = Typography(),
    val searchBar: TextStyle = TextStyle(FontWeight.Normal, 14.sp),
    val collectionTitleLarge: TextStyle = TextStyle(FontWeight.Bold, 20.sp),
    val collectionTitleSmall: TextStyle = TextStyle(FontWeight.SemiBold, 12.sp)

) {
    val displayLarge = material.displayLarge
    val displayMedium = material.displayMedium
    val displaySmall = material.displaySmall
    val headlineLarge = material.headlineLarge
    val headlineMedium = material.headlineMedium
    val headlineSmall = material.headlineSmall
    val titleLarge = material.titleLarge
    val titleMedium = material.titleMedium
    val titleSmall = material.titleSmall
    val bodyLarge = material.bodyLarge
    val bodyMedium = material.bodyMedium
    val bodySmall = material.bodySmall
    val labelLarge = material.labelLarge
    val labelMedium = material.labelMedium
    val labelSmall = material.labelSmall
}