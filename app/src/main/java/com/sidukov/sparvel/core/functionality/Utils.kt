package com.sidukov.sparvel.core.functionality

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)
@Composable
fun GetStoragePermission(
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                permissionState.launchMultiplePermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    when {
        permissionState.allPermissionsGranted -> onPermissionGranted()
        permissionState.shouldShowRationale -> onPermissionDenied()
        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
            // Text(text = "Permission fully denied. Go to settings to enable")
        }
    }
}

fun List<Track>.toMusicCollection() = this.groupBy { it.album }.map {
    val unnamedArtist = "Unknown artist"
    val unnamedAlbum = "Unknown album"
    MusicCollection(
        it.key,
        it.value[0].coverId,
        if (it.value[0].composer == unnamedArtist || it.value[0].album == unnamedAlbum) unnamedAlbum else it.value[0].album,
        it.value
    )
}

fun List<Track>.toJsonString(): String =
    Gson().toJson(this.toTypedArray(), Array<Track>::class.java).toString()

fun String?.toTrackList() =
    Gson().fromJson(
        URLDecoder.decode(this, StandardCharsets.UTF_8.toString()),
        Array<Track>::class.java
    ).toList()