package com.sidukov.sparvel.features.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sidukov.sparvel.core.functionality.Route
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.features.album.AlbumScreen
import com.sidukov.sparvel.features.album.AlbumsScreen
import com.sidukov.sparvel.features.equalizer.EqualizerScreen
import com.sidukov.sparvel.features.home.HomeScreen
import com.sidukov.sparvel.features.home.HomeViewModel
import com.sidukov.sparvel.features.library.LibraryScreen
import com.sidukov.sparvel.features.playlist.NewPlaylistScreen
import com.sidukov.sparvel.features.playlist.PlaylistScreen
import com.sidukov.sparvel.features.playlist.PlaylistsScreen
import com.sidukov.sparvel.features.splash.SplashScreen
import com.sidukov.sparvel.features.track.AddTracksScreen
import com.sidukov.sparvel.features.track.EditTrackInfoScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainerScreen(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    onAppThemeChanged: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                        .background(SparvelTheme.colors.drawer),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    DrawerContent(
                        onAppLanguageClicked = {

                        },
                        onSoundSettingsClicked = {

                        },
                        onColorThemeClicked = onAppThemeChanged
                    )
                }
            }
        },
        content = {
            AppNavigationGraph(navController, viewModelProvider) {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    )
}

@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    onMenuClicked: () -> Unit
) {
    NavHost(navController, startDestination = Route.SPLASH) {
        composable(Route.SPLASH) {
            // add images loading there and return list of bitmaps to home screen
            SplashScreen(navController)
        }
        drawerContainerGraph(navController, viewModelProvider, onMenuClicked)
        composable(Route.NEW_PLAYLIST) {
            NewPlaylistScreen()
        }
        composable(Route.EDIT_TRACK_INFO) {
            EditTrackInfoScreen()
        }
        composable(Route.ADD_TRACKS) {
            AddTracksScreen()
        }
        composable(Route.EQUALIZER) {
            EqualizerScreen()
        }
    }
}

fun NavGraphBuilder.drawerContainerGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    onMenuClicked: () -> Unit
) {
    navigation(startDestination = Route.HOME, route = Route.DRAWER_CONTAINER) {
        composable(Route.HOME) {
            HomeScreen(
                viewModelProvider[HomeViewModel::class.java],
                navController,
                onMenuClicked
            )
        }
        composable(Route.PLAYLISTS) {
            PlaylistsScreen()
        }
        composable(Route.PLAYLIST) {
            PlaylistScreen()
        }
        composable(Route.ALBUMS) {
            AlbumsScreen()
        }
        composable(Route.ALBUM) {
            AlbumScreen()
        }
        composable(Route.LIBRARY) {
            LibraryScreen()
        }
    }
}

@Composable
fun DrawerContent(
    onAppLanguageClicked: () -> Unit,
    onSoundSettingsClicked: () -> Unit,
    onColorThemeClicked: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 35.dp, end = 35.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(top = 125.dp),
                text = stringResource(com.sidukov.sparvel.R.string.app_name),
                style = SparvelTheme.typography.drawerTitle,
                color = SparvelTheme.colors.drawerText
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                DrawerSheetItem(
                    img = com.sidukov.sparvel.R.drawable.ic_globe,
                    text = com.sidukov.sparvel.R.string.app_language_action
                ) {
                    onAppLanguageClicked()
                }
                DrawerSheetItem(
                    img = com.sidukov.sparvel.R.drawable.ic_equalizer,
                    text = com.sidukov.sparvel.R.string.sound_settings_action
                ) {
                    onSoundSettingsClicked()
                }
                DrawerSheetItem(
                    img = com.sidukov.sparvel.R.drawable.ic_crescent,
                    text = com.sidukov.sparvel.R.string.color_theme_action
                ) {
                    onColorThemeClicked()
                }
            }
            Text(
                modifier = Modifier.padding(bottom = 50.dp),
                text = stringResource(com.sidukov.sparvel.R.string.version_label),
                style = SparvelTheme.typography.appVersion,
                color = SparvelTheme.colors.drawerText
            )
        }
    }
}

@Composable
fun DrawerSheetItem(img: Int, text: Int, onItemClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(
                indication = rememberRipple(),
                interactionSource = remember {
                    MutableInteractionSource()
                },
                role = Role.Button,
                onClick = {
                    onItemClicked()
                }
            )
    ) {
        Image(
            modifier = Modifier.size(25.dp),
            painter = painterResource(img),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = stringResource(text),
            style = SparvelTheme.typography.drawerText,
            color = SparvelTheme.colors.drawerText
        )
    }
}