package com.sidukov.sparvel.features.main

import android.graphics.Bitmap
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sidukov.sparvel.SparvelApplication
import com.sidukov.sparvel.core.functionality.AppTheme
import com.sidukov.sparvel.core.functionality.Route
import com.sidukov.sparvel.core.functionality.background
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
import kotlin.math.hypot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainerScreen(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    windowBitmap: Bitmap?,
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

    windowBitmap?.let { screenshot ->

        val (width, height) = with(LocalConfiguration.current) {
            with(LocalDensity.current) { screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx() }
        }
        val maxRadiusPx = hypot(width, height)
        var radius by remember { mutableStateOf(maxRadiusPx) }
        val animatedRadius = remember { Animatable(maxRadiusPx) }

        LaunchedEffect(false) {
            animatedRadius.animateTo(0f, animationSpec = tween()) {
                radius = value/2
            }
            // reset the initial value after finishing animation
            // animatedRadius.snapTo(maxRadiusPx)
        }
        Card(
            modifier = Modifier
                .size(Dp(radius)),
            shape = RoundedCornerShape(size = radius),
        ) {
            Image(
                bitmap = screenshot.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                alpha = radius / maxRadiusPx
            )
        }
    }
}

@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    onMenuClicked: () -> Unit
) {
    NavHost(navController, startDestination = Route.DRAWER_CONTAINER) {
        composable(Route.SPLASH) {
            SplashScreen()
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