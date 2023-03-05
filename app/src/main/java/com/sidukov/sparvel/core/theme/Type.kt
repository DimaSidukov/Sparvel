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

val InterFontFamily = FontFamily(
    Font(R.font.inter_light, FontWeight.Light)
)

internal fun LatoTextStyle(weight: FontWeight, size: TextUnit = 16.sp) = TextStyle(
    fontFamily = LatoFontFamily,
    fontWeight = weight,
    fontSize = size
)

data class SparvelTypography(
    val material: Typography = Typography(),
    val appName: TextStyle = LatoTextStyle(FontWeight.Light, 35.sp),
    val searchBar: TextStyle = LatoTextStyle(FontWeight.Normal, 14.sp),
    val collectionTitleLarge: TextStyle = LatoTextStyle(FontWeight.Bold, 20.sp),
    val collectionTitleMedium: TextStyle = LatoTextStyle(FontWeight.Normal, 16.sp),
    val collectionTitleSmall: TextStyle = LatoTextStyle(FontWeight.SemiBold, 12.sp),
    val collectionInfo: TextStyle = LatoTextStyle(FontWeight.Light, 12.sp),
    val trackNameSmall: TextStyle = LatoTextStyle(FontWeight.SemiBold, 14.sp),
    val trackNameMedium: TextStyle = LatoTextStyle(FontWeight.SemiBold, 22.sp),
    val artistMedium: TextStyle = LatoTextStyle(FontWeight.Light, 16.sp),
    val drawerTitle: TextStyle = LatoTextStyle(FontWeight.Black, 24.sp),
    val drawerText: TextStyle = LatoTextStyle(FontWeight.Medium, 18.sp),
    val appVersion: TextStyle = LatoTextStyle(FontWeight.Light, 15.sp),
    val permissionDenied: TextStyle = LatoTextStyle(FontWeight.Medium, 16.sp),
    val toolbarTitle: TextStyle = LatoTextStyle(FontWeight.Bold, 24.sp),
    val progressTimestamp: TextStyle = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
)