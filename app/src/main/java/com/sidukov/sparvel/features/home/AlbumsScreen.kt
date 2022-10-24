package com.sidukov.sparvel.features.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.ui.CollectionScreen

@Composable
fun AlbumsScreen(
    navController: NavController,
    albums: List<MusicCollection>,
    onNavigatedBack: () -> Unit
) {
    CollectionScreen(
        modifier = Modifier.padding(horizontal = 30.dp),
        sectionName = stringResource(R.string.albums_label),
        collectionList = albums
    )
    BackHandler(onBack = onNavigatedBack)
}