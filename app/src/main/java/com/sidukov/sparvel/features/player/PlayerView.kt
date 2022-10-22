package com.sidukov.sparvel.features.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.normalize
import com.sidukov.sparvel.core.functionality.toMinutesAndSeconds
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.core.ui.HQImageOrPlaceholder
import com.sidukov.sparvel.core.ui.Toolbar
import kotlinx.coroutines.launch

@Composable
fun PlayerView(
    track: Track,
    image: ImageBitmap?,
    onSettingsClicked: () -> Unit
) {

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp +
                WindowInsets.systemBars.getTop(LocalDensity.current).dp
    val minHeight = 120.dp

    var shouldMoveDown by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var playbackTimestamp by remember { mutableStateOf(0.3f) }

    val scope = rememberCoroutineScope()

    DraggableView(
        screenHeight = screenHeight,
        minHeight = minHeight,
        shouldMoveDown = shouldMoveDown
    ) { currentHeight, isLayoutExpanded ->
        val ratio = 0.35f
        val heightNormalized = currentHeight.value.normalize(screenHeight.value, minHeight.value)
        val alpha = if (heightNormalized < ratio) 0f else heightNormalized.normalize(1f, ratio)

        SideEffect {
            if (isLayoutExpanded) {
                shouldMoveDown = false
            }
        }
        Column(
            modifier = Modifier.alpha(alpha.normalize(1f, 0.3f))
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
                            shouldMoveDown = true
                        }
                    },
                    onActionClicked = onSettingsClicked
                )
            }
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column {
                    TrackInfo(
                        name = track.name,
                        artist = track.artist,
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    PlayerProgress(playbackTimestamp) { value ->
                        playbackTimestamp = value
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Timestamps(
                        start = 0,
                        end = track.duration
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    PlayerController(
                        isPlaying = isPlaying,
                        onRepeatClicked = {

                        },
                        onPreviousClicked = {

                        },
                        onPlayClicked = {
                            isPlaying = !isPlaying
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
}

@Composable
fun Timestamps(
    start: Int,
    end: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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

@Composable
fun PlayerProgress(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    ThinnerSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp),
        colors = SliderDefaults.colors(
            thumbColor = SparvelTheme.colors.progressTrack,
            activeTrackColor = SparvelTheme.colors.progressTrack,
            inactiveTrackColor = SparvelTheme.colors.progress
        )
    )
}

@Composable
fun PlayerController(
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
            .padding(bottom = 70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRepeatClicked, modifier = Modifier.size(30.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_repeat),
                contentDescription = null,
                tint = playerActionColor,
            )
        }
        IconButton(onClick = onPreviousClicked, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_player_arrow),
                contentDescription = null,
                tint = playerActionColor
            )
        }
        PlayButton(
            isPlaying = isPlaying,
            onClick = onPlayClicked,
            backgroundColor = playerActionColor,
            iconColor = SparvelTheme.colors.playerIcon
        )
        IconButton(onClick = onNextClicked, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_player_arrow),
                contentDescription = null,
                tint = playerActionColor,
                modifier = Modifier.rotate(180f)
            )
        }
        IconButton(onClick = onCurrentPlaylistClicked, modifier = Modifier.size(30.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_playlist),
                contentDescription = null,
                tint = playerActionColor
            )
        }
    }
}

@Composable
fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color,
    iconColor: Color
) {
    // https://stackoverflow.com/questions/71232511/jetpack-compose-play-pause-animation
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
