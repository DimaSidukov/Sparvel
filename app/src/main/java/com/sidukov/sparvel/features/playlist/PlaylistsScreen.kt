package com.sidukov.sparvel.features.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sidukov.sparvel.core.functionality.exitScreenWithAction

@Composable
fun PlaylistsScreen(
    navController: NavController,
    onNavigatedBack: () -> Unit = { }
) {

    Column(
        modifier = Modifier.padding(start = 30.dp, end = 30.dp)
    ) {

    }

    exitScreenWithAction(onNavigatedBack)

}