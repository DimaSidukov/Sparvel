package com.sidukov.sparvel.features.playlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sidukov.sparvel.core.functionality.SelectedTrackPadding

@Composable
fun PlaylistsScreen(
    navController: NavController,
    isTrackSelected: Boolean,
    onNavigatedBack: () -> Unit = { }
) {

    Column(
        modifier = Modifier.padding(start = 30.dp, end = 30.dp)
    ) {
        SelectedTrackPadding(
            isTrackSelected = isTrackSelected,
            padding = 100.dp
        )
    }
    BackHandler(onBack = onNavigatedBack)
}