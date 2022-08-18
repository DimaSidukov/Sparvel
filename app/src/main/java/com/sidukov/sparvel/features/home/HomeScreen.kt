package com.sidukov.sparvel.features.home

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.model.TrackItem
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.ui.AddPlaylistItem
import com.sidukov.sparvel.core.ui.CollectionSection
import com.sidukov.sparvel.core.ui.SearchBar
import com.sidukov.sparvel.core.ui.TrackList
import kotlinx.coroutines.launch

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                        .background(SparvelTheme.colors.textPlaceholder),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    DrawerContent(
                        onAppLanguageClicked = {

                        },
                        onSoundSettingsClicked = {

                        },
                        onColorThemeClicked = {

                        }
                    )
                }
            }
        },
        content = {
            HomeScreenContent {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    )
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
                text = stringResource(R.string.app_name),
                style = SparvelTheme.typography.drawerTitle,
                color = SparvelTheme.colors.drawerText
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                DrawerSheetItem(img = R.drawable.ic_globe, text = R.string.app_language_action) {
                    onAppLanguageClicked()
                }
                DrawerSheetItem(img = R.drawable.ic_equalizer, text = R.string.sound_settings_action) {
                    onSoundSettingsClicked()
                }
                DrawerSheetItem(img = R.drawable.ic_crescent, text = R.string.color_theme_action) {
                    onColorThemeClicked()
                }
            }
            Text(
                modifier = Modifier.padding(bottom = 50.dp),
                text = stringResource(R.string.version_label),
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

@Composable
fun HomeScreenContent(
    onMenuClicked: () -> Unit
) {
    Column(verticalArrangement = Arrangement.Top) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 30.dp, top = 30.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                onClick = {
                    onMenuClicked()
                },
            ) {
                Icon(
                    modifier = Modifier.scale(1.0f),
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = null,
                    tint = SparvelTheme.colors.secondary
                )
            }

            SearchBar(modifier = Modifier.padding(start = 20.dp)) {
                // do something when text updated
            }
        }
        TrackList(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            itemList = mockTracks,
            onItemClicked = {
                // open player fragment and start track
            }
        ) {
            Column {
                CollectionSection(
                    sectionName = stringResource(R.string.playlists_label),
                    itemList = mockPlaylists,
                    onItemClicked = {
                        // on playlist clicked
                    }
                ) {
                    AddPlaylistItem {
                        // on add playlist Clicked
                    }
                    Spacer(modifier = Modifier.padding(end = 30.dp))
                }
                Spacer(modifier = Modifier.height(30.dp))
                CollectionSection(
                    sectionName = stringResource(R.string.albums_label),
                    itemList = mockAlbums,
                    onItemClicked = {
                        // on album clicked
                    }
                )
                Text(
                    modifier = Modifier.padding(bottom = 20.dp, top = 30.dp),
                    text = stringResource(R.string.library_label),
                    style = SparvelTheme.typography.collectionTitleLarge,
                    color = SparvelTheme.colors.secondary
                )
            }
        }
    }
}

// Mock data
val mockPlaylists = listOf("Happiness", "In bad mood", "Relentless", "Vivid")
val mockAlbums =
    listOf("Revival", "One step further", "X_X", "n u m b", "Pieces of non uttered tales")
val mockTracks = generateSequence {
    TrackItem("Random", "human", "empty")
}.take(50).toMutableList()