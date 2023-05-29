package com.sidukov.sparvel.features.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.apiGreaterThan
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.functionality.normalize
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.widgets.ImageType
import com.sidukov.sparvel.core.widgets.SparvelImage
import com.sidukov.sparvel.core.widgets.Toolbar
import com.sidukov.sparvel.features.home.HomeViewModel
import com.sidukov.sparvel.features.home.PlayerState
import kotlinx.coroutines.launch

private const val ANDROID_12 = android.os.Build.VERSION_CODES.S
private const val animationDuration = 300

enum class SwipeState {
    COLLAPSED, EXPANDED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerBottomSheet(
    viewModel: HomeViewModel,
    iconColor: Color,
) {

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp +
                WindowInsets.systemBars.getTop(LocalDensity.current).dp
    val minHeight = 150.dp
    val swipeState = rememberSwipeableState(initialValue = SwipeState.COLLAPSED)
    val scope = rememberCoroutineScope()
    val playbackPosition = viewModel.playbackPosition.collectAsState(initial = 0f)
    val playbackTimestamp = viewModel.playbackTimestamp.collectAsState(initial = 0L)
    val gradientAnimationDuration = 20000
    val gradientStartX = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx() / 1.4f
    }
    val gradientStartY = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx() / 1.3f
    }
    val gradientX = if (apiGreaterThan(ANDROID_12)) rememberInfiniteTransition().animateFloat(
        initialValue = gradientStartX - 300f,
        targetValue = gradientStartX - 300f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = gradientAnimationDuration
                gradientStartX - 300f at 0 with FastOutSlowInEasing
                gradientStartX - 500f at gradientAnimationDuration / 4 with FastOutSlowInEasing
                gradientStartX + 100f at gradientAnimationDuration / 3 with FastOutSlowInEasing
                gradientStartX - 500f at gradientAnimationDuration / 2 with FastOutSlowInEasing
                gradientStartX at gradientAnimationDuration with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    ).value else 0f
    val gradientY = if (apiGreaterThan(ANDROID_12)) rememberInfiniteTransition().animateFloat(
        initialValue = gradientStartY + 300f,
        targetValue = gradientStartY + 300f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = gradientAnimationDuration
                gradientStartY + 300f at 0 with FastOutSlowInEasing
                gradientStartY + 500f at gradientAnimationDuration / 4 with FastOutSlowInEasing
                gradientStartY + 400f at gradientAnimationDuration / 3 with FastOutSlowInEasing
                gradientStartY - 100f at gradientAnimationDuration / 2 with FastOutSlowInEasing
                gradientStartY + 300f at gradientAnimationDuration with FastOutSlowInEasing
            }
        )
    ).value else 0f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = swipeState.offset.value.dp)
                .align(Alignment.BottomCenter)
                .shadow(20.dp, clip = true, shape = RoundedCornerShape(5, 5))
                .clip(
                    RoundedCornerShape(5, 5)
                )
                .swipeable(
                    state = swipeState,
                    orientation = Orientation.Vertical,
                    resistance = ResistanceConfig(0f, 0f, 0f),
                    anchors = mapOf(
                        screenHeight.value - minHeight.value to SwipeState.COLLAPSED,
                        0f to SwipeState.EXPANDED
                    )
                ),
        ) {
            val collapsedRatio = 0.6f
            val expandedRatio = 0.8f
            val heightNormalized by remember {
                derivedStateOf {
                    swipeState.offset.value.normalize(screenHeight.value - minHeight.value, 0f)
                }
            }
            val collapsedAlpha by remember {
                derivedStateOf {
                    if (heightNormalized < collapsedRatio) 0f else heightNormalized.normalize(
                        1f,
                        collapsedRatio
                    )
                }
            }
            val expandedAlpha by remember {
                derivedStateOf {
                    if (heightNormalized > expandedRatio) 0f else heightNormalized.normalize(
                        0f,
                        expandedRatio
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SparvelTheme.colors.background)
                    .blur(150.dp)
                    .background(
                        if (apiGreaterThan(ANDROID_12)) Brush.sweepGradient(
                            SparvelTheme.colors.playerBackground,
                            Offset(gradientX, gradientY)
                        ) else {
                            Brush.verticalGradient(
                                SparvelTheme.colors.playerBackground
                            )
                        }
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(0.7f)
                    .clickable(
                        enabled = swipeState.currentValue == SwipeState.COLLAPSED,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            scope.launch {
                                swipeState.animateTo(
                                    SwipeState.EXPANDED, tween(animationDuration)
                                )
                            }
                        }
                    )
            )
            CollapsedPlayer(
                viewModel = viewModel,
                alpha = collapsedAlpha,
                height = minHeight / 2,
                track = viewModel.uiState.selectedTrack!!,
            )
            ExpandedPlayer(
                viewModel = viewModel,
                alpha = expandedAlpha,
                playbackPosition = playbackPosition.value,
                playbackTimestamp = playbackTimestamp.value,
                iconColor = iconColor,
                onCollapseClicked = {
                    scope.launch {
                        swipeState.animateTo(SwipeState.COLLAPSED, tween(animationDuration))
                    }
                },
                onSliderValueChanged = {
                    viewModel.onPositionUpdated(it)
                },
                onSliderValueChangeFinished = {
                    viewModel.seek(playbackPosition.value)
                }
            )
        }
    }
    if (swipeState.currentValue == SwipeState.EXPANDED) {
        BackHandler {
            scope.launch {
                swipeState.animateTo(SwipeState.COLLAPSED)
            }
        }
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
        Icon(
            painter = rememberAnimatedVectorPainter(
                animatedImageVector = AnimatedImageVector.animatedVectorResource(
                    id = R.drawable.anim_play_pause
                ), atEnd = viewModel.uiState.playerState == PlayerState.Playing
            ),
            contentDescription = null,
            tint = SparvelTheme.colors.playerActions,
            modifier = Modifier
                .alpha(alpha)
                .padding(top = 10.dp, end = 15.dp)
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
                .size(60.dp)
                .padding(18.dp)
        )
    }
}


@Composable
fun ExpandedPlayer(
    viewModel: HomeViewModel,
    alpha: Float,
    playbackPosition: Float,
    playbackTimestamp: Long,
    iconColor: Color,
    onCollapseClicked: () -> Unit,
    onSliderValueChanged: (Float) -> Unit,
    onSliderValueChangeFinished: () -> Unit,
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
                    value = playbackPosition,
                    onValueChange = onSliderValueChanged,
                    onValueChangeFinished = onSliderValueChangeFinished
                )
                Spacer(modifier = Modifier.height(8.dp))
                Timestamps(
                    start = playbackTimestamp,
                    end = viewModel.uiState.selectedTrack!!.duration,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                PlayerController(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    playerState = viewModel.uiState.playerState,
                    onRepeatClicked = {
                        viewModel.onRepeatClicked()
                    },
                    onPreviousClicked = {
                        viewModel.onPreviousClicked()
                    },
                    onPlayClicked = {
                        viewModel.updatePlayerState()
                    },
                    onNextClicked = {
                        viewModel.onNextClicked()
                    },
                    onCurrentPlaylistClicked = {
                        viewModel.onCurrentPlaylistClicked()
                    }
                )
            }
        }
    }
}