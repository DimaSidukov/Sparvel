package com.sidukov.sparvel.features.home

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

                val duration = 300
                val density = LocalDensity.current
                val screenWidth = (LocalConfiguration.current.screenWidthDp / 2).dp

                // start animation:
                // slideInHorizontally(tween(duration)) { with(density) { -screenWidth.roundToPx() } } + fadeIn(tween(duration))

//                var screenState by remember { mutableStateOf(LIBRARY as HomeScreen) }
//                LaunchedEffect(screenState) {
//                    screenState = uiState.currentScreen
//                }
                when (uiState.currentScreen) {
                    FULL -> {
                        HomeScreen(
                            navController = navController,
                            trackList = newTrackList.filter(query),
                            onPlaylistSectionClicked = {
                                viewModel.setHomeScreen(PLAYLISTS)
                            },
                            onAlbumSectionClicked = {
                                viewModel.setHomeScreen(ALBUMS)
                            }
                        )
                    }
                    PLAYLISTS -> {
                        PlaylistsScreen(
                            navController = navController,
                            onNavigatedBack = {
                                viewModel.setHomeScreen(FULL)
                            }
                        )
                    }
                    ALBUMS -> {
                        AlbumsScreen(
                            navController = navController,
                            albums = trackList.toMusicCollection(),
                            onNavigatedBack = {
                                viewModel.setHomeScreen(FULL)
                            }
                        )
                    }
                    LIBRARY -> {

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