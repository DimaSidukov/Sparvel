package com.sidukov.sparvel.core.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.sidukov.sparvel.R

val Lato = FontFamily(
    Font(R.font.lato_black, FontWeight.Black),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_semibold, FontWeight.SemiBold),
    Font(R.font.lato_medium, FontWeight.Medium),
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_thin, FontWeight.Thin)
)

internal fun TextStyle(weight: FontWeight, size: TextUnit = 16.sp) = TextStyle(
    fontFamily = Lato,
    fontWeight = weight,
    fontSize = size
)

class SparvelTypography {
    val black = TextStyle(FontWeight.Black, 24.sp)
    val bold = TextStyle(FontWeight.Bold, 20.sp)
    val semiBold = TextStyle(FontWeight.SemiBold, 20.sp)
    val medium = TextStyle(FontWeight.Medium, 18.sp)
    val regular = TextStyle(FontWeight.Normal)
    val light = TextStyle(FontWeight.Light)
    val thin = TextStyle(FontWeight.Thin)
}