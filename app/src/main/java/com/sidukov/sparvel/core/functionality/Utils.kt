package com.sidukov.sparvel.core.functionality

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore.Images.Media.getBitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sidukov.sparvel.BuildConfig
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme

var appVersion: String = "Version ${BuildConfig.VERSION_NAME}"

inline fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier =
    if (condition) then(modifier(this)) else this

fun Modifier.background(color: Any): Modifier =
    when (color) {
        is Brush -> this.then(Modifier.background(color))
        is Color -> this.then(Modifier.background(color))
        else -> this
    }

fun List<Track>.toMusicCollection() = this.groupBy { it.album }.map {
    val unnamedArtist = "Unknown artist"
    val unnamedAlbum = "Unknown album"
    MusicCollection(
        it.key,
        it.value[0].coverId,
        if (it.value[0].artist == unnamedArtist || it.value[0].album == unnamedAlbum) unnamedAlbum else it.value[0].album,
        it.value,
        it.value[0].year
    )
}

fun List<Track>.filter(query: String): List<Track> {
    val words = query.trim().split("\\s+".toRegex()).toList()
    return this.filter { track ->
        track.name.containsQueryOrWords(query, words)
                || track.album.containsQueryOrWords(query, words)
                || track.artist.containsQueryOrWords(query, words)
    }
}

private fun String.containsQueryOrWords(query: String, words: List<String>) =
    this.contains(query, true) || words.any { this.contains(it, true) }


fun Modifier.systemBarsPadding() = composed {
    this.windowInsetsPadding(
        WindowInsets.systemBars.only(
            WindowInsetsSides.Top + WindowInsetsSides.Bottom
        )
    )
}

fun Modifier.applyGradient(needGradient: Boolean, gradient: Brush) = this
    .applyIf(needGradient) {
        graphicsLayer { alpha = 0.99f }
            .drawWithContent {
                drawContent()
                drawRect(gradient, blendMode = BlendMode.DstIn)
            }
    }
    .clip(RoundedCornerShape(10.dp))

fun Float.normalize(max: Float, min: Float): Float = (this - min) / (max - min)

fun Int.toMinutesAndSeconds() = String.format("%d:%02d", this / 60, this % 60)

fun Color.firstFiveDigits(): Float = this.value.toString().take(5).toFloat()

@Composable
fun ImageBitmap?.deriveIconColor(): Color {
    return this?.asAndroidBitmap()?.let { bitmap ->
        val stableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val pixels = IntArray(stableBitmap.height * stableBitmap.width)
        stableBitmap.getPixels(pixels, 0, stableBitmap.width, 40, 30, 200, 200)
        val dominantColor = Color(pixels.average().toInt()).firstFiveDigits()

        if (dominantColor <= 0) return SparvelTheme.colors.navigation
        val isCloserToBlack =
            (dominantColor - Color.Black.firstFiveDigits()) /
                    (Color.White.firstFiveDigits() - Color.Black.firstFiveDigits())

        if (isCloserToBlack > 0.98) Color.Black else Color.White
    }
        ?: SparvelTheme.colors.navigation
}

fun String.decodeBitmap(cr: ContentResolver): ImageBitmap? {
    return try {
        when {
            Build.VERSION.SDK_INT < 28 -> getBitmap(cr, this.toUri())
            else -> {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(cr, this.toUri()))
            }
        }
    } catch (e: Exception) {
        null
    }?.asImageBitmap()
}

@Composable
fun SelectedTrackPadding(
    isTrackSelected: Boolean = false,
    padding: Dp = 60.dp,
    defaultPadding: Dp = 10.dp
) =
    Spacer(
        modifier = Modifier.height(
            if (isTrackSelected) padding else defaultPadding
        )
    )