package com.sidukov.sparvel.core.functionality

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.model.Track

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
fun GetStoragePermission() {
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


    // do something if user pressed "Not Allow"
//    when {
//        permissionState.hasPermission -> {
//            Text(text = "Reading external permission is granted")
//        }
//        permissionState.shouldShowRationale -> {
//            Column {
//                Text(text = "Reading external permission is required by this app")
//            }
//        }
//        !permissionState.hasPermission && !permissionState.shouldShowRationale -> {
//            Text(text = "Permission fully denied. Go to settings to enable")
//        }
//    }
}

fun Context.getBitmapOrNull(contentUri: Uri, id: Long): Bitmap? {
    return try {
        when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                ContentUris.withAppendedId(contentUri, id)
            )
            else -> {
                val source = ImageDecoder.createSource(
                    this.contentResolver,
                    ContentUris.withAppendedId(contentUri, id)
                )
                ImageDecoder.decodeBitmap(source)
            }
        }
    } catch (e: Exception) {
        null
    }
}

fun List<Track>.toMusicCollection() = this.groupBy { it.album }.map {
    val unnamedArtist = "Unknown artist"
    val unnamedAlbum = "Unknown album"
    MusicCollection(
        it.key,
        it.value[0].cover,
        if (it.value[0].composer == unnamedArtist || it.value[0].album == unnamedAlbum) unnamedAlbum else it.value[0].album,
        it.value
    )
}