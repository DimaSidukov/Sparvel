package com.sidukov.sparvel.features.home

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.Route
import com.sidukov.sparvel.core.functionality.GetStoragePermission
import com.sidukov.sparvel.core.model.TrackItem
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.ui.AddPlaylistItem
import com.sidukov.sparvel.core.ui.CollectionSection
import com.sidukov.sparvel.core.ui.SearchBar
import com.sidukov.sparvel.core.ui.TrackList
import java.lang.Math.hypot
import kotlin.math.hypot

@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavHostController,
    onMenuClicked: () -> Unit,
) {

    GetStoragePermission()

    val uiState = viewModel.uiState

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
            itemList = uiState.trackList,
            onItemClicked = {
                // open player fragment and start track
            }
        ) {
            Column(modifier = Modifier.padding(start = 30.dp)) {
                CollectionSection(
                    modifier = Modifier.padding(end = 30.dp),
                    sectionName = stringResource(R.string.playlists_label),
                    itemList = mockPlaylists,
                    onItemClicked = {
                        navController.navigate(Route.PLAYLISTS)
                    }
                ) {
                    AddPlaylistItem {
                        navController.navigate(Route.NEW_PLAYLIST)
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                }
                Spacer(modifier = Modifier.height(30.dp))
                CollectionSection(
                    modifier = Modifier.padding(end = 30.dp),
                    sectionName = stringResource(R.string.albums_label),
                    itemList = mockAlbums,
                    onItemClicked = {
                        navController.navigate(Route.ALBUMS)
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
    listOf(
        "Revival",
        "One step further",
        "X_X",
        "n u m b",
        "Pieces of non uttered tales sdlfj ksdfkjhsdfkjh sdjfhj"
    )