package com.sidukov.sparvel.features.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.ui.SectionName
import com.sidukov.sparvel.core.ui.TrackList

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    tracks: List<Track>,
    isTrackSelected: Boolean,
    onTrackClicked: (Track) -> Unit,
    onNavigatedBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        TrackList(
            itemList = tracks,
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
            isTrackSelected = isTrackSelected,
            onItemClicked = onTrackClicked
        ) {
            SectionName(
                sectionName = stringResource(R.string.library_label),
                modifier = Modifier.padding(start = 30.dp, bottom = 40.dp)
            )
        }
    }
    BackHandler(onBack = onNavigatedBack)
}