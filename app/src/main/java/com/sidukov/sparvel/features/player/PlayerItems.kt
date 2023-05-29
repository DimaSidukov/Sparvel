package com.sidukov.sparvel.features.player

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.functionality.toMinutesAndSeconds
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.features.home.PlayerState
import kotlin.math.ceil

@Composable
fun Timestamps(
    start: Long,
    end: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = ceil(start / 1000.0).toInt().toMinutesAndSeconds(),
            style = SparvelTheme.typography.progressTimestamp,
            color = SparvelTheme.colors.text
        )
        Text(
            text = ceil(end / 1000.0).toInt().toMinutesAndSeconds(),
            style = SparvelTheme.typography.progressTimestamp,
            color = SparvelTheme.colors.text
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackInfo(
    name: String,
    artist: String,
    modifier: Modifier = Modifier
) {
    val animationDelay = 1000
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.basicMarquee(
                delayMillis = animationDelay
            ),
            text = name,
            style = SparvelTheme.typography.trackNameMedium,
            color = SparvelTheme.colors.text
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.basicMarquee(
                delayMillis = animationDelay
            ),
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
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
) = Slider(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    onValueChangeFinished = onValueChangeFinished,
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

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onRepeatClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onCurrentPlaylistClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerButton(
            source = R.drawable.ic_repeat,
            contentDescription = null,
            onClick = onRepeatClicked
        )
        PlayerButton(
            source = R.drawable.ic_player_arrow,
            contentDescription = null,
            onClick = onPreviousClicked
        )
        Icon(
            painter = rememberAnimatedVectorPainter(
                animatedImageVector = AnimatedImageVector.animatedVectorResource(
                    id = R.drawable.anim_play_pause
                ), atEnd = playerState == PlayerState.Playing
            ),
            contentDescription = null,
            tint = SparvelTheme.colors.playerIcon,
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(50))
                .background(SparvelTheme.colors.playerActions)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        color = SparvelTheme.colors.text
                    ),
                    onClick = onPlayClicked
                )
                .padding(20.dp),
        )
        PlayerButton(
            source = R.drawable.ic_player_arrow,
            contentDescription = null,
            modifier = Modifier.rotate(180f),
            onClick = onNextClicked
        )
        PlayerButton(
            source = R.drawable.ic_playlist,
            contentDescription = null,
            onClick = onCurrentPlaylistClicked
        )
    }
}

@Composable
fun PlayerButton(
    source: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(source),
        contentDescription = contentDescription,
        tint = SparvelTheme.colors.playerActions,
        modifier = Modifier
            .then(modifier)
            .size(50.dp)
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    radius = 25.dp
                ),
                onClick = onClick
            )
            .padding(13.dp)
    )
}