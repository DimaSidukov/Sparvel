package com.sidukov.sparvel.features.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.sidukov.sparvel.core.functionality.filter
import com.sidukov.sparvel.core.functionality.toMusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.widgets.HomeMenuPanel
import com.sidukov.sparvel.features.home.HomeScreen.*
import com.sidukov.sparvel.features.player.PlayerView
import com.sidukov.sparvel.features.playlist.PlaylistsScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreenContainer(
    viewModel: HomeViewModel,
    navController: NavHostController,
    onMenuClicked: () -> Unit,
) {
    GetStoragePermission(
        onPermissionGranted = {
            HomeScreenApprovedState(
                viewModel = viewModel,
                navController = navController,
                onMenuClicked = onMenuClicked
            )
        },
        onPermissionDenied = {
            HomeScreenDeniedState()
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreenApprovedState(
    viewModel: HomeViewModel,
    navController: NavHostController,
    onMenuClicked: () -> Unit
) {
    val uiState = viewModel.uiState
    val scope = rememberCoroutineScope()

    var trackList by remember { mutableStateOf<List<Track>>(emptyList()) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        trackList = viewModel.readTracks()
    }

    HomeMenuPanel(
        onMenuClicked = onMenuClicked,
        onTextUpdated = {
            query = it
        }
    ) {
        trackList = trackList.filter(query)
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
                    HomeScreen(
                        navController = navController,
                        trackList = trackList,
                        isTrackSelected = uiState.selectedTrack != null,
                        onPlaylistSectionClicked = {
                            viewModel.setScreen(PLAYLISTS)
                        },
                        onAlbumSectionClicked = {
                            viewModel.setScreen(ALBUMS)
                        },
                        onLibrarySectionClicked = {
                            scope.launch {
                                viewModel.setScreen(LIBRARY)
                            }
                        },
                        onTrackClicked = {
                            scope.launch {
                                viewModel.showPlayer(it)
                            }
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
                        albums = trackList.toMusicCollection(),
                        onNavigatedBack = {
                            viewModel.setScreen(FULL)
                        }
                    )
                }
                LIBRARY -> {
                    LibraryScreen(
                        tracks = trackList,
                        isTrackSelected = uiState.selectedTrack != null,
                        onTrackClicked = {
                            scope.launch {
                                viewModel.showPlayer(it)
                            }
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
        PlayerView(
            track = it,
            image = it.coverId.decodeBitmap(),
            onSettingsClicked = {

            }
        )
    }
}

@Composable
fun HomeScreenDeniedState() {
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
    }
}