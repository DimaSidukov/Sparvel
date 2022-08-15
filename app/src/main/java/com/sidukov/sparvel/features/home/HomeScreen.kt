package com.sidukov.sparvel.features.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.ui.AddPlaylistItem
import com.sidukov.sparvel.core.ui.CollectionItem
import com.sidukov.sparvel.core.ui.SearchBar
import kotlinx.coroutines.launch

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
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
                    Text("test")
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    onMenuClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .then(Modifier.size(26.dp, 23.dp))
                    .align(Alignment.CenterVertically),
                onClick = {
                    onMenuClicked()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = null,
                    tint = SparvelTheme.colors.secondary
                )
            }

            SearchBar(modifier = Modifier.padding(start = 30.dp)) {
                // do something when text updated
            }
        }

        Text(
            modifier = Modifier.padding(start = 30.dp),
            text = stringResource(R.string.playlists_label),
            style = SparvelTheme.typography.collectionTitleLarge,
            color = SparvelTheme.colors.secondary
        )

        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            LazyRow(
                modifier = Modifier.padding(start = 30.dp, top = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                items(mockPlaylists) { item ->
                    CollectionItem(
                        playlistName = item,
                        // temporary measure
                        playlistImage = painterResource(R.drawable.ic_launcher_background),
                        needGradient = true
                    )
                }
                item {
                    AddPlaylistItem {

                    }
                    Spacer(modifier = Modifier.padding(end = 30.dp))
                }
            }
        }
    }
}

// Mock data
val mockPlaylists = listOf("Happiness", "In bad mood", "Relentless", "Vivid")