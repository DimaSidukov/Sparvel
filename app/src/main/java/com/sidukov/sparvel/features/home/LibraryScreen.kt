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
import com.sidukov.sparvel.core.widgets.SectionName
import com.sidukov.sparvel.core.widgets.TrackList

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    tracks: List<Track>,
    selectedTrackId: String?,
    isAudioPlaying: Boolean,
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
            selectedTrackId = selectedTrackId,
            isAudioPlaying = isAudioPlaying,
            needShowSettings = true,
            onItemClicked = onTrackClicked
        ) {
            SectionName(
                sectionName = stringResource(R.string.library_label),
                isClickEnabled = false,
                modifier = Modifier.padding(start = 25.dp, bottom = 35.dp)
            )
        }
    }
    BackHandler(onBack = onNavigatedBack)
}