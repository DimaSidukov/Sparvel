package com.sidukov.sparvel.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.GetStoragePermission
import com.sidukov.sparvel.core.functionality.filter
import com.sidukov.sparvel.core.functionality.toMusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.ui.HomeMenuPanel
import com.sidukov.sparvel.features.home.HomeScreen.*
import com.sidukov.sparvel.features.playlist.PlaylistsScreen

@Composable
fun HomeScreenContainer(
    viewModel: HomeViewModel,
    navController: NavHostController,
    trackList: List<Track>,
    onMenuClicked: () -> Unit,
) {

    val uiState = viewModel.uiState

    GetStoragePermission(
        onPermissionGranted = {
            var newTrackList by remember { mutableStateOf(trackList) }
            if (trackList.isEmpty()) {
                LaunchedEffect(true) {
                    newTrackList = viewModel.readTracks()
                }
            }
            var query by remember { mutableStateOf("") }

            HomeMenuPanel(
                onMenuClicked = onMenuClicked,
                onTextUpdated = {
                    query = it
                }
            ) {
                when (uiState.currentScreen) {
                    FULL -> {
                        HomeScreen(
                            navController = navController,
                            trackList = newTrackList.filter(query),
                            onPlaylistSectionClicked = {
                                viewModel.setScreen(PLAYLISTS)
                            },
                            onAlbumSectionClicked = {
                                viewModel.setScreen(ALBUMS)
                            }
                        )
                    }
                    PLAYLISTS -> {
                        viewModel.showNewScreen()
                        SwipeFromLeftAnimation(isVisible = uiState.isNewScreenVisible) {
                            PlaylistsScreen(
                                navController = navController,
                                onNavigatedBack = {
                                    viewModel.setScreenAndDisableAnimation(FULL)
                                }
                            )
                        }
                    }
                    ALBUMS -> {
                        viewModel.showNewScreen()
                        SwipeFromLeftAnimation(
                            isVisible = uiState.isNewScreenVisible
                        ) {
                            AlbumsScreen(
                                navController = navController,
                                albums = trackList.toMusicCollection(),
                                onNavigatedBack = {
                                    viewModel.setScreenAndDisableAnimation(FULL)
                                }
                            )
                        }
                    }
                    LIBRARY -> {
                        viewModel.showNewScreen()
                        SwipeFromLeftAnimation(isVisible = uiState.isNewScreenVisible) {
                            LibraryScreen()
                        }
                    }
                }
            }
        },
        onPermissionDenied = {
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
    )
}

@Composable
fun SwipeFromLeftAnimation(
    isVisible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val duration = 100
    val density = LocalDensity.current
    val screenWidth = (LocalConfiguration.current.screenWidthDp / 2).dp

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(tween(duration)) { with(density) { -screenWidth.roundToPx() / 2 } } + fadeIn(
            tween(duration / 2)
        ),
        content = content
    )
}