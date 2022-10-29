package com.sidukov.sparvel.features.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.normalize
import com.sidukov.sparvel.core.functionality.toMinutesAndSeconds
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.widgets.HQImageOrPlaceholder
import com.sidukov.sparvel.core.widgets.ImageOrPlaceholder
import com.sidukov.sparvel.core.widgets.Toolbar
import kotlinx.coroutines.launch

@Composable
fun PlayerView(
    track: Track,
    image: ImageBitmap?,
    iconColor: Color,
    onSettingsClicked: () -> Unit
) {

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp +
                WindowInsets.systemBars.getTop(LocalDensity.current).dp
    val minHeight = 120.dp

    var shouldMoveDown by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
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
            isPlaying = isPlaying,
            onClick = { isPlaying = !isPlaying }
        )
        ExpandedPlayerLayout(
            alpha = expandedAlpha,
            playbackTimestamp = playbackTimestamp,
            isPlaying = isPlaying,
            track = track,
            image = image,
            iconColor = iconColor,
            onCollapseClicked = { shouldMoveDown = true },
            onSettingsClicked = onSettingsClicked,
            onSliderValueChanged = { playbackTimestamp = it },
            onRepeatClicked = { },
            onPreviousClicked = { },
            onPlayClicked = { isPlaying = !isPlaying },
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
    isPlaying: Boolean,
    onClick: () -> Unit
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
                isPlaying = isPlaying,
                onClick = onClick
            )
        }
    }
}


@Composable
fun ExpandedPlayerLayout(
    alpha: Float,
    playbackTimestamp: Float,
    isPlaying: Boolean,
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
                    isPlaying = isPlaying,
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

@Composable
fun Timestamps(
    start: Int,
    end: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = start.toMinutesAndSeconds(),
            style = SparvelTheme.typography.progressTimestamp,
            color = SparvelTheme.colors.text
        )
        Text(
            text = end.toMinutesAndSeconds(),
            style = SparvelTheme.typography.progressTimestamp,
            color = SparvelTheme.colors.text
        )
    }
}

@Composable
fun TrackInfo(
    name: String,
    artist: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            style = SparvelTheme.typography.trackNameMedium,
            color = SparvelTheme.colors.text
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = artist,
            style = SparvelTheme.typography.artistMedium,
            color = SparvelTheme.colors.text
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProgress(
    value: Float,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit
) = Slider(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    colors = SliderDefaults.colors(
        activeTrackColor = SparvelTheme.colors.activeTrack,
        inactiveTrackColor = SparvelTheme.colors.inactiveTrack,
    ),
    thumb = remember {
        {
            SliderDefaults.Thumb(
                interactionSource = remember { MutableInteractionSource() },
                thumbSize = DpSize(8.dp, 8.dp),
                modifier = Modifier.offset(y = 6.dp, x = 4.dp),
                colors = SliderDefaults.colors(
                    thumbColor = SparvelTheme.colors.thumb
                )
            )
        }
    }
)

@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onRepeatClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onCurrentPlaylistClicked: () -> Unit
) {

    val playerActionColor = SparvelTheme.colors.playerActions

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRepeatClicked, modifier = Modifier.size(30.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_repeat),
                contentDescription = null,
                tint = SparvelTheme.colors.playerActions,
            )
        }
        IconButton(onClick = onPreviousClicked, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_player_arrow),
                contentDescription = null,
                tint = SparvelTheme.colors.playerActions
            )
        }
        EncircledPlayButton(
            isPlaying = isPlaying,
            onClick = onPlayClicked
        )
        IconButton(onClick = onNextClicked, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_player_arrow),
                contentDescription = null,
                tint = SparvelTheme.colors.playerActions,
                modifier = Modifier.rotate(180f)
            )
        }
        IconButton(onClick = onCurrentPlaylistClicked, modifier = Modifier.size(30.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_playlist),
                contentDescription = null,
                tint = SparvelTheme.colors.playerActions
            )
        }
    }
}

@Composable
fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit
) {

    val iconColor = SparvelTheme.colors.playerActions

    val x1 by remember { mutableStateOf(40f) }
    val y1 by remember { mutableStateOf(30f) }
    val x2 by remember { mutableStateOf(80f) }
    val y2 by remember { mutableStateOf(56.7f) }
    val x3 by remember { mutableStateOf(40f) }
    val y3 by remember { mutableStateOf(83.4f) }

    Canvas(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(50))
            .clickable {
                onClick()
            }
    ) {
        val path = Path()
        path.reset()
        path.lineTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x3, y3)
        path.lineTo(x1, y1)
        path.close()

        drawPath(
            path = path,
            color = iconColor,

            )
    }
}

@Composable
fun EncircledPlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    // https://stackoverflow.com/questions/71232511/jetpack-compose-play-pause-animation

    val backgroundColor = SparvelTheme.colors.playerActions
    val iconColor = SparvelTheme.colors.playerIcon

    Canvas(
        modifier = Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick()
            }
    ) {
        drawCircle(
            color = backgroundColor,
            radius = 100f
        )
    }
}
