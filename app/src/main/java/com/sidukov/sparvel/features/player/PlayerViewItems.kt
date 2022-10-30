package com.sidukov.sparvel.features.player

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.functionality.toMinutesAndSeconds
import com.sidukov.sparvel.core.theme.SparvelTheme
import kotlinx.coroutines.launch

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

@Preview
@Composable
fun PlayButton(
    isPlaying: Boolean = false,
    onClick: () -> Unit = { }
) {

    val iconColor = SparvelTheme.colors.playerActions

    com.sidukov.sparvel.core.widgets.PlayButton(isPlaying = isPlaying, color = iconColor) {
        isPlaying != isPlaying
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