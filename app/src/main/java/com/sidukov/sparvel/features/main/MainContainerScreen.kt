package com.sidukov.sparvel.features.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.Screens
import com.sidukov.sparvel.core.functionality.appVersion
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.features.album.AlbumScreen
import com.sidukov.sparvel.features.equalizer.EqualizerScreen
import com.sidukov.sparvel.features.home.HomeScreenContainer
import com.sidukov.sparvel.features.home.HomeViewModel
import com.sidukov.sparvel.features.home.PlaylistScreen
import com.sidukov.sparvel.features.playlist.NewPlaylistScreen
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
            ModalDrawerSheet(
                windowInsets = WindowInsets(top = 0.dp),
                modifier = Modifier.fillMaxHeight(),
                drawerContainerColor = Color.Transparent
            ) {
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
                        onColorThemeClicked = onAppThemeChanged,
                        onHelpFeedbackClicked = {

                        }
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
    NavHost(navController, startDestination = Screens.Splash.route) {
        composable(Screens.Splash.route) {
            SplashScreen(navController)
        }
        drawerContainerGraph(navController, viewModelProvider, onMenuClicked)
        composable(Screens.NewPlaylist.route) {
            NewPlaylistScreen()
        }
        composable(Screens.EditTrackInfo.route) {
            EditTrackInfoScreen()
        }
        composable(Screens.AddTracks.route) {
            AddTracksScreen()
        }
        composable(Screens.Equalizer.route) {
            EqualizerScreen()
        }
    }
}

fun NavGraphBuilder.drawerContainerGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    onMenuClicked: () -> Unit
) {
    navigation(startDestination = Screens.Home.route, route = Screens.DrawerContainer.route) {
        composable(route = Screens.Home.route) {
            HomeScreenContainer(
                viewModelProvider[HomeViewModel::class.java],
                navController,
                onMenuClicked
            )
        }
        composable(Screens.Playlist.route) {
            PlaylistScreen()
        }
        composable(Screens.Album.route) {
            AlbumScreen()
        }
    }
}

@Composable
fun DrawerContent(
    onAppLanguageClicked: () -> Unit,
    onSoundSettingsClicked: () -> Unit,
    onColorThemeClicked: () -> Unit,
    onHelpFeedbackClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        Text(
            modifier = Modifier.padding(top = 125.dp, start = 35.dp),
            text = stringResource(R.string.app_name),
            style = SparvelTheme.typography.drawerTitle,
            color = SparvelTheme.colors.drawerText,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DrawerSheetItem(
                img = R.drawable.ic_globe,
                text = R.string.app_language_action
            ) {
                onAppLanguageClicked()
            }
            DrawerSheetItem(
                img = R.drawable.ic_equalizer,
                text = R.string.sound_settings_action
            ) {
                onSoundSettingsClicked()
            }
            DrawerSheetItem(
                img = R.drawable.ic_crescent,
                text = R.string.color_theme_action
            ) {
                onColorThemeClicked()
            }
            DrawerSheetItem(
                img = R.drawable.ic_info,
                text = R.string.help_and_feedback_label
            ) {
                onHelpFeedbackClicked()
            }
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 50.dp, start = 35.dp),
            text = appVersion,
            style = SparvelTheme.typography.appVersion,
            color = SparvelTheme.colors.drawerText
        )
    }
}

@Composable
fun DrawerSheetItem(img: Int, text: Int, onItemClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = Color.Black
                ),
                role = Role.Button
            ) { onItemClicked() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(img),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(text),
                style = SparvelTheme.typography.drawerText,
                color = SparvelTheme.colors.drawerText
            )
        }
    }
}