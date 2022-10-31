package com.sidukov.sparvel.features.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.normalize
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.widgets.HQImageOrPlaceholder
import com.sidukov.sparvel.core.widgets.ImageOrPlaceholder
import com.sidukov.sparvel.core.widgets.PlayButton
import com.sidukov.sparvel.core.widgets.Toolbar
import com.sidukov.sparvel.features.home.PlayerState
import kotlinx.coroutines.launch

@Composable
fun PlayerView(
    track: Track,
    image: ImageBitmap?,
    playerState: PlayerState,
    iconColor: Color,
    onPlayButtonClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp +
                WindowInsets.systemBars.getTop(LocalDensity.current).dp
    val minHeight = 120.dp

    var shouldMoveDown by remember { mutableStateOf(false) }
    var playbackTimestamp by remember { mutableStateOf(0.3f) }

    BackHandler {
        shouldMoveDown = true
    }

    DraggableView(
        screenHeight = screenHeight,
        minHeight = minHeight,
        shouldMoveDown = shouldMoveDown
    ) { currentHeight, isLayoutExpanded ->
        val expandedRatio = 0.2f
        val collapsedRatio = 0.4f
        val heightNormalized = currentHeight.value.normalize(screenHeight.value, minHeight.value)
        val expandedAlpha =
            if (heightNormalized < expandedRatio) 0f else heightNormalized.normalize(
                1f,
                expandedRatio
            )
        val collapsedAlpha =
            if (heightNormalized > collapsedRatio) 0f else heightNormalized.normalize(
                0f,
                collapsedRatio
            )

        SideEffect {
            if (isLayoutExpanded) {
                shouldMoveDown = false
            }
        }
        CollapsedPlayerLayout(
            alpha = collapsedAlpha,
            height = minHeight / 2,
            track = track,
            playerState = playerState,
            onPlayerButtonClicked = onPlayButtonClicked
        )
        ExpandedPlayerLayout(
            alpha = expandedAlpha,
            playbackTimestamp = playbackTimestamp,
            playerState = playerState,
            track = track,
            image = image,
            iconColor = iconColor,
            onCollapseClicked = { shouldMoveDown = true },
            onSettingsClicked = onSettingsClicked,
            onSliderValueChanged = { playbackTimestamp = it },
            onRepeatClicked = { },
            onPreviousClicked = { },
            onPlayClicked = onPlayButtonClicked,
            onNextClicked = { },
            onCurrentPlaylistClicked = { }
        )
    }
}

@Composable
fun CollapsedPlayerLayout(
    alpha: Float,
    height: Dp,
    track: Track,
    playerState: PlayerState,
    onPlayerButtonClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .alpha(alpha),
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            ImageOrPlaceholder(
                imageUrl = track.coverId,
                imageSize = 40,
                needGradient = false
            )
            Column(
                modifier = Modifier
                    .height(40.dp)
                    .padding(start = 8.dp, end = 35.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    track.name,
                    style = SparvelTheme.typography.trackNameSmall.copy(fontSize = 12.sp),
                    color = SparvelTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(
                        R.string.artist_album_label,
                        track.artist,
                        track.album
                    ),
                    style = SparvelTheme.typography.collectionInfo.copy(fontSize = 11.sp),
                    color = SparvelTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            PlayButton(
                playerState = playerState,
                color = SparvelTheme.colors.playerActions,
                onButtonClick = onPlayerButtonClicked
            )
        }
    }
}


@Composable
fun ExpandedPlayerLayout(
    alpha: Float,
    playbackTimestamp: Float,
    playerState: PlayerState,
    track: Track,
    image: ImageBitmap?,
    iconColor: Color,
    onCollapseClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onSliderValueChanged: (Float) -> Unit,
    onRepeatClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onCurrentPlaylistClicked: () -> Unit
) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.alpha(alpha)
    ) {
        Box {
            HQImageOrPlaceholder(
                image = image,
                imageSize = 500,
                needGradient = true
            )
            Toolbar(
                navigationIcon = R.drawable.ic_arrow,
                actionIcon = R.drawable.ic_equalizer,
                onNavigationClicked = {
                    scope.launch {
                        onCollapseClicked()
                    }
                },
                onActionClicked = onSettingsClicked,
                iconColor = iconColor
            )
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column {
                TrackInfo(
                    name = track.name,
                    artist = track.artist,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(50.dp))
                PlayerProgress(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    value = playbackTimestamp
                ) { value ->
                    onSliderValueChanged(value)
                    // playbackTimestamp = value
                }
                Spacer(modifier = Modifier.height(8.dp))
                Timestamps(
                    start = 0,
                    end = track.duration,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                PlayerController(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    playerState = playerState,
                    onRepeatClicked = onRepeatClicked,
                    onPreviousClicked = onPreviousClicked,
                    onPlayClicked = onPlayClicked,
                    onNextClicked = onNextClicked,
                    onCurrentPlaylistClicked = onCurrentPlaylistClicked
                )
            }
        }
    }
}
