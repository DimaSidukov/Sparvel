package com.sidukov.sparvel.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.GetStoragePermission
import com.sidukov.sparvel.core.functionality.Screens
import com.sidukov.sparvel.core.functionality.toMusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.ui.AddPlaylistItem
import com.sidukov.sparvel.core.ui.CollectionSection
import com.sidukov.sparvel.core.ui.SearchBar
import com.sidukov.sparvel.core.ui.TrackList

@Composable
fun HomeScreen(
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
            // probably will need to remake it into toolbar
            Column(verticalArrangement = Arrangement.Top) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, bottom = 30.dp, top = 30.dp)
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.CenterVertically),
                        onClick = {
                            onMenuClicked()
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(23.dp),
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = null,
                            tint = SparvelTheme.colors.navigation
                        )
                    }

                    SearchBar(modifier = Modifier.padding(start = 25.dp)) {
                        // do something when text updated
                    }
                }
                TrackList(
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                    itemList = newTrackList,
                    onItemClicked = {
                        // open player fragment and start track
                    }
                ) {
                    Column(modifier = Modifier.padding(start = 30.dp)) {
                        CollectionSection(
                            modifier = Modifier.padding(end = 30.dp),
                            sectionName = stringResource(R.string.playlists_label),
                            itemList = emptyList(),
                            onItemClicked = {
                                navController.navigate(Screens.Playlists.route)
                            }
                        ) {
                            AddPlaylistItem {
                                navController.navigate(Screens.NewPlaylist.route)
                            }
                            Spacer(modifier = Modifier.width(30.dp))
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        CollectionSection(
                            modifier = Modifier.padding(end = 30.dp),
                            sectionName = stringResource(R.string.albums_label),
                            itemList = newTrackList.toMusicCollection(),
                            onItemClicked = {
                                navController.navigate(Screens.Albums.route)
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
                    color = SparvelTheme.colors.permissionDenied
                )
            }
        }
    )
}