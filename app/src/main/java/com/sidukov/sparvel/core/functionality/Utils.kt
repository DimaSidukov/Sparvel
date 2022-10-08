package com.sidukov.sparvel.core.functionality

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore.Images.Media.getBitmap
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.gson.Gson
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier =
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
        if (it.value[0].composer == unnamedArtist || it.value[0].album == unnamedAlbum) unnamedAlbum else it.value[0].album,
        it.value,
        it.value[0].year
    )
}

fun List<Track>.toJsonString(): String =
    Gson().toJson(this.toTypedArray(), Array<Track>::class.java).toString()

fun String?.toTrackList() =
    Gson().fromJson(
        URLDecoder.decode(this, StandardCharsets.UTF_8.toString()),
        Array<Track>::class.java
    ).toList()

@SuppressLint("ComposableNaming")
@Composable
fun exitScreenWithAction(action: () -> Unit) {
    val backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val currentOnBackPressed by rememberUpdatedState(newValue = action)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(
        key1 = backPressedDispatcher
    ) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose { backCallback.remove() }
    }
}

fun List<Track>.filter(query: String): List<Track> {
    val words = query.trim().split("\\s+".toRegex()).toList()
    return this.filter { track ->
        track.title.containsQueryOrWords(query, words)
                || track.album.containsQueryOrWords(query, words)
                || track.composer.containsQueryOrWords(query, words)
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

@Composable
fun String.decodeBitmap(): ImageBitmap? {
    val cr = LocalContext.current.contentResolver
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

fun Modifier.applyGradient(needGradient: Boolean, gradient: Brush) = this
    .applyIf(needGradient) {
        graphicsLayer { alpha = 0.99f }
            .drawWithContent {
                drawContent()
                drawRect(gradient, blendMode = BlendMode.DstIn)
            }
    }
    .clip(RoundedCornerShape(10.dp))

fun Any.normalize(max: Any, min: Any): Float? = when (this) {
    is Dp -> (this - min as Dp) / (max as Dp - min)
    is Float -> (this - min as Float) / (max as Float - min)
    else -> null
}