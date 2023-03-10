package com.sidukov.sparvel.features.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.functionality.normalize
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.widgets.ImageType
import com.sidukov.sparvel.core.widgets.SparvelImage
import com.sidukov.sparvel.core.widgets.Toolbar
import com.sidukov.sparvel.features.home.HomeViewModel
import com.sidukov.sparvel.features.home.PlayerState

@Composable
fun PlayerBottomSheet(
    viewModel: HomeViewModel,
    iconColor: Color,
) {

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp +
                WindowInsets.systemBars.getTop(LocalDensity.current).dp
    val minHeight = 150.dp

    var shouldMoveDown by remember { mutableStateOf(false) }
    var playbackTimestamp by remember { mutableStateOf(0.3f) }

    BackHandler {
        shouldMoveDown = true
    }

    DraggableBottomSheet(
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
        CollapsedPlayer(
            viewModel = viewModel,
            alpha = collapsedAlpha,
            height = minHeight / 2,
            track = viewModel.uiState.selectedTrack!!,
        )
        ExpandedPlayer(
            viewModel = viewModel,
            alpha = expandedAlpha,
            playbackTimestamp = playbackTimestamp,
            iconColor = iconColor,
            onCollapseClicked = { shouldMoveDown = true },
            onSliderValueChanged = { playbackTimestamp = it },
        )
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun CollapsedPlayer(
    viewModel: HomeViewModel,
    alpha: Float,
    height: Dp,
    track: Track
) {

    var shouldRunPlayAnimation by remember {
        mutableStateOf(false)
    }

    Box {
        Column(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .alpha(alpha),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(40.dp)
                    .clip(RoundedCornerShape(50))
                    .align(Alignment.CenterHorizontally)
                    .background(SparvelTheme.colors.playerDragStrokeColor)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SparvelImage(
                        imageUri = track.coverId,
                        imageSize = 40,
                        needGradient = false
                    )
                    Column(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(start = 8.dp, end = 45.dp),
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
            }
        }
        Box(
            modifier = Modifier
                .alpha(alpha)
                .padding(top = 10.dp, end = 15.dp)
                .size(60.dp)
                .clip(RoundedCornerShape(50))
                .align(Alignment.CenterEnd)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        radius = 30.dp
                    ),
                    onClick = {
                        shouldRunPlayAnimation = true
                        viewModel.updatePlayerState()
                        shouldRunPlayAnimation = false
                    }
                )
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center),
                painter = rememberAnimatedVectorPainter(
                    animatedImageVector = AnimatedImageVector.animatedVectorResource(
                        id = R.drawable.anim_play_pause
                    ), atEnd = viewModel.uiState.playerState == PlayerState.Playing
                ),
                contentDescription = null,
                colorFilter = ColorFilter.tint(SparvelTheme.colors.playerActions)
            )
        }
    }
}


@Composable
fun ExpandedPlayer(
    viewModel: HomeViewModel,
    alpha: Float,
    playbackTimestamp: Float,
    iconColor: Color,
    onCollapseClicked: () -> Unit,
    onSliderValueChanged: (Float) -> Unit,
) {

    Box(
        modifier = Modifier.alpha(alpha)
    ) {
        Box {
            Box(
                modifier = Modifier.height(500.dp)
            ) {
                SparvelImage(
                    imageUri = viewModel.uiState.selectedTrack!!.coverId,
                    imageSize = 500,
                    needGradient = true,
                    imageType = ImageType.Borderless
                )
            }
            if (alpha > 0f) {
                Toolbar(
                    navigationIcon = R.drawable.ic_arrow,
                    actionIcon = R.drawable.ic_equalizer,
                    onNavigationClicked = {
                        onCollapseClicked()
                    },
                    onActionClicked = {
                        // What to do when clicking on settings
                    },
                    iconColor = iconColor
                )
            }
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
                    name = viewModel.uiState.selectedTrack!!.name,
                    artist = viewModel.uiState.selectedTrack!!.artist,
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
                    end = viewModel.uiState.selectedTrack!!.duration,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                PlayerController(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    playerState = viewModel.uiState.playerState,
                    onRepeatClicked = {

                    },
                    onPreviousClicked = {

                    },
                    onPlayClicked = {
                        viewModel.updatePlayerState()
                    },
                    onNextClicked = {

                    },
                    onCurrentPlaylistClicked = {

                    }
                )
            }
        }
    }
}
