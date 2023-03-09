package com.sidukov.sparvel.features.home

import android.Manifest
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.decodeBitmap
import com.sidukov.sparvel.core.functionality.deriveIconColor
import com.sidukov.sparvel.core.functionality.filter
import com.sidukov.sparvel.core.functionality.toMusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.widgets.MenuSearchPanel
import com.sidukov.sparvel.features.home.HomeScreen.*
import com.sidukov.sparvel.features.player.PlayerBottomSheet
import com.sidukov.sparvel.features.playlist.PlaylistsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavHostController,
    onMenuClicked: () -> Unit
) {
    val uiState = viewModel.uiState
    val cr = LocalContext.current.contentResolver

    var trackList by remember { mutableStateOf<List<Track>>(emptyList()) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        trackList = viewModel.readTracks()
    }

    MenuSearchPanel(
        onMenuClicked = onMenuClicked,
        onTextUpdated = { query = it }
    ) {

        val filteredTrackList by remember { derivedStateOf { trackList.filter(query) } }

        AnimatedContent(
            targetState = uiState.currentScreen,
            transitionSpec = {
                if (targetState == FULL) {
                    slideInHorizontally { height -> height } + fadeIn() with
                            slideOutHorizontally { height -> -height } + fadeOut()
                } else {
                    slideInHorizontally { height -> -height } + fadeIn() with
                            slideOutHorizontally { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { screen ->
            when (screen) {
                FULL -> {
                    TrackListScreen(
                        navController = navController,
                        trackList = filteredTrackList,
                        isTrackSelected = uiState.selectedTrack != null,
                        onPlaylistSectionClicked = {
                            viewModel.setScreen(PLAYLISTS)
                        },
                        onAlbumSectionClicked = {
                            viewModel.setScreen(ALBUMS)
                        },
                        onLibrarySectionClicked = {
                            viewModel.setScreen(LIBRARY)
                        },
                        onTrackClicked = {
                            viewModel.showPlayer(it)
                        }
                    )
                }
                PLAYLISTS -> {
                    PlaylistsScreen(
                        navController = navController,
                        isTrackSelected = uiState.selectedTrack != null,
                        onNavigatedBack = {
                            viewModel.setScreen(FULL)
                        }
                    )
                }
                ALBUMS -> {
                    AlbumsScreen(
                        navController = navController,
                        isTrackSelected = uiState.selectedTrack != null,
                        albums = filteredTrackList.toMusicCollection(),
                        onNavigatedBack = {
                            viewModel.setScreen(FULL)
                        }
                    )
                }
                LIBRARY -> {
                    LibraryScreen(
                        tracks = filteredTrackList,
                        isTrackSelected = uiState.selectedTrack != null,
                        onTrackClicked = {
                            viewModel.showPlayer(it)
                        },
                        onNavigatedBack = {
                            viewModel.setScreen(FULL)
                        }
                    )
                }
            }
        }
    }
    uiState.selectedTrack?.let {
        var img by remember { mutableStateOf<ImageBitmap?>(null) }
        LaunchedEffect(it) {
            img = it.coverId.decodeBitmap(cr)
        }

        PlayerBottomSheet(
            viewModel = viewModel,
            iconColor = img.deriveIconColor()
        )
    }
}

@Composable
fun PermissionDeniedMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.permission_denied_label),
            style = SparvelTheme.typography.permissionDenied,
            color = SparvelTheme.colors.permissionDenied,
            textAlign = TextAlign.Center
        )
    }
}

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
    }
}