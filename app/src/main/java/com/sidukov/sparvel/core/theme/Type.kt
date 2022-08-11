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

val SparvelTypography = Typography(
    displayLarge = TextStyle(FontWeight.Black, 32.sp),
    displayMedium = TextStyle(FontWeight.Black, 24.sp),
    displaySmall = TextStyle(FontWeight.Black, 20.sp),
    headlineLarge = TextStyle(FontWeight.Bold, 18.sp),
    headlineMedium = TextStyle(FontWeight.Bold),
    headlineSmall = TextStyle(FontWeight.Bold, 14.sp),
    titleLarge = TextStyle(FontWeight.Bold, 24.sp),
    titleMedium = TextStyle(FontWeight.Medium, 20.sp),
    titleSmall = TextStyle(FontWeight.Medium, 16.sp),
    bodyLarge = TextStyle(FontWeight.Normal, 14.sp),
    bodyMedium = TextStyle(FontWeight.Normal, 14.sp),
    bodySmall = TextStyle(FontWeight.Light, 14.sp),
    labelLarge = TextStyle(FontWeight.Normal),
    labelMedium = TextStyle(FontWeight.Medium, 14.sp),
    labelSmall = TextStyle(FontWeight.Light, 12.sp)
)