package com.sidukov.sparvel.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.Screens
import com.sidukov.sparvel.core.functionality.toMusicCollection
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.widgets.AddPlaylistItem
import com.sidukov.sparvel.core.widgets.CollectionSection
import com.sidukov.sparvel.core.widgets.SectionName
import com.sidukov.sparvel.core.widgets.TrackList

@Composable
fun TrackListScreen(
    navController: NavHostController,
    trackList: List<Track>,
    isTrackSelected: Boolean,
    onPlaylistSectionClicked: () -> Unit,
    onAlbumSectionClicked: () -> Unit,
    onLibrarySectionClicked: () -> Unit,
    onTrackClicked: (Track) -> Unit,
) {
    Column {
        TrackList(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            itemList = trackList,
            isTrackSelected = isTrackSelected,
            onItemClicked = onTrackClicked
        ) {
            Column(modifier = Modifier.padding(start = 25.dp)) {
                CollectionSection(
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
                    sectionName = stringResource(R.string.albums_label),
                    itemList = trackList.toMusicCollection(),
                    onSectionNameClicked = onAlbumSectionClicked,
                    onItemClicked = {
                        // here open screen of specific album
                        // navController.navigate(Screens.Albums.route)
                    }
                )
                Box(
                    modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                ) {
                    SectionName(
                        sectionName = stringResource(R.string.library_label),
                        onItemClicked = onLibrarySectionClicked
                    )
                }
            }
        }
    }
}