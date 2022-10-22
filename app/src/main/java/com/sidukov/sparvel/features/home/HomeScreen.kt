package com.sidukov.sparvel.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.Screens
import com.sidukov.sparvel.core.functionality.toMusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.ui.AddPlaylistItem
import com.sidukov.sparvel.core.ui.CollectionSection
import com.sidukov.sparvel.core.ui.TrackList

@Composable
fun HomeScreen(
    isTrackSelected: Boolean,
    navController: NavHostController,
    trackList: List<Track>,
    onPlaylistSectionClicked: () -> Unit,
    onAlbumSectionClicked: () -> Unit,
    onTrackClicked: (Track) -> Unit,
) {
    Column {
        TrackList(
            isTrackSelected = isTrackSelected,
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            itemList = trackList,
            onItemClicked = onTrackClicked
        ) {
            Column(modifier = Modifier.padding(start = 30.dp)) {
                CollectionSection(
                    modifier = Modifier.padding(end = 30.dp),
                    sectionName = stringResource(R.string.playlists_label),
                    itemList = emptyList(),
                    onSectionNameClicked = onPlaylistSectionClicked,
                    onItemClicked = {
                        // here open screen of specific playlist
                        // navController.navigate(Screens.Playlists.route)
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
                    itemList = trackList.toMusicCollection(),
                    onSectionNameClicked = onAlbumSectionClicked,
                    onItemClicked = {
                        // here open screen of specific album
                        // navController.navigate(Screens.Albums.route)
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