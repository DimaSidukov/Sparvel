package com.sidukov.sparvel.core.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.theme.SparvelTheme
import com.sidukov.sparvel.features.home.PlayerState
import com.sidukov.sparvel.features.home.PlayerState.Playing

@Composable
fun PlayButton(
    playerState: PlayerState,
    onButtonClick: () -> Unit
) {

    val animationTime = 100
    val iconColor = SparvelTheme.colors.playerActions

    val firstStick = ButtonPoints(
        x1 = animateFloatAsState(
            if (playerState == Playing) 65f else 80f,
            tween(animationTime)
        ),
        y1 = animateFloatAsState(
            if (playerState == Playing) 60f else 55f,
            tween(animationTime)
        ),
        x2 = animateFloatAsState(
            if (playerState == Playing) 75f else 80f,
            tween(animationTime)
        ),
        y2 = animateFloatAsState(
            if (playerState == Playing) 60f else 55f,
            tween(animationTime)
        ),
        x3 = animateFloatAsState(
            if (playerState == Playing) 75f else 80f,
            tween(animationTime)
        ),
        y3 = animateFloatAsState(
            if (playerState == Playing) 103.4f else 98.4f,
            tween(animationTime)
        ),
        x4 = animateFloatAsState(
            if (playerState == Playing) 65f else 55f,
            tween(animationTime)
        ),
        y4 = animateFloatAsState(
            if (playerState == Playing) 103.4f else 98.4f,
            tween(animationTime)
        )
    )

    val secondStick = ButtonPoints(
        x1 = animateFloatAsState(
            if (playerState == Playing) 85f else 80f,
            tween(animationTime)
        ),
        y1 = animateFloatAsState(
            if (playerState == Playing) 60f else 55f,
            tween(animationTime)
        ),
        x2 = animateFloatAsState(
            if (playerState == Playing) 95f else 80f,
            tween(animationTime)
        ),
        y2 = animateFloatAsState(
            if (playerState == Playing) 60f else 55f,
            tween(animationTime)
        ),
        x3 = animateFloatAsState(
            if (playerState == Playing) 95f else 105f,
            tween(animationTime)
        ),
        y3 = animateFloatAsState(
            if (playerState == Playing) 103.4f else 98.4f,
            tween(animationTime)
        ),
        x4 = animateFloatAsState(
            if (playerState == Playing) 85f else 80f,
            tween(animationTime)
        ),
        y4 = animateFloatAsState(
            if (playerState == Playing) 103.4f else 98.4f,
            tween(animationTime)
        )
    )

    val rotation by animateFloatAsState(
        targetValue = if (playerState == Playing) 0f else 90f,
        animationSpec = tween(animationTime)
    )

    val path1 = Path().apply {
        moveTo(firstStick.x1.value, firstStick.y1.value)
        lineTo(firstStick.x2.value, firstStick.y2.value)
        lineTo(firstStick.x3.value, firstStick.y3.value)
        lineTo(firstStick.x4.value, firstStick.y4.value)
        close()
    }

    val path2 = Path().apply {
        moveTo(secondStick.x1.value, secondStick.y1.value)
        lineTo(secondStick.x2.value, secondStick.y2.value)
        lineTo(secondStick.x3.value, secondStick.y3.value)
        lineTo(secondStick.x4.value, secondStick.y4.value)
        close()
    }

    Canvas(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(50))
            .rotate(rotation)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    radius = 20.dp
                ),
                onClick = onButtonClick
            )
    ) {

        drawPath(
            path = path1,
            color = iconColor
        )
        drawPath(
            path = path2,
            color = iconColor
        )
    }
}

@Composable
fun EncircledPlayButton(
    playerState: PlayerState,
    onButtonClick: () -> Unit
) {

    val animationTime = 100
    val backgroundColor = SparvelTheme.colors.playerActions
    val iconColor = SparvelTheme.colors.playerIcon

    val firstStick = ButtonPoints(
        x1 = animateFloatAsState(
            if (playerState == Playing) 78f else 103f,
            tween(animationTime)
        ),
        y1 = animateFloatAsState(
            if (playerState == Playing) 73f else 70f,
            tween(animationTime)
        ),
        x2 = animateFloatAsState(
            if (playerState == Playing) 92f else 103f,
            tween(animationTime)
        ),
        y2 = animateFloatAsState(
            if (playerState == Playing) 73f else 70f,
            tween(animationTime)
        ),
        x3 = animateFloatAsState(
            if (playerState == Playing) 92f else 103f,
            tween(animationTime)
        ),
        y3 = animateFloatAsState(
            if (playerState == Playing) 133.76f else 120f,
            tween(animationTime)
        ),
        x4 = animateFloatAsState(
            if (playerState == Playing) 78f else 70f,
            tween(animationTime)
        ),
        y4 = animateFloatAsState(
            if (playerState == Playing) 133.76f else 120f,
            tween(animationTime)
        )
    )

    val secondStick = ButtonPoints(
        x1 = animateFloatAsState(
            if (playerState == Playing) 111f else 103f,
            tween(animationTime)
        ),
        y1 = animateFloatAsState(
            if (playerState == Playing) 73f else 70f,
            tween(animationTime)
        ),
        x2 = animateFloatAsState(
            if (playerState == Playing) 125f else 103f,
            tween(animationTime)
        ),
        y2 = animateFloatAsState(
            if (playerState == Playing) 73f else 70f,
            tween(animationTime)
        ),
        x3 = animateFloatAsState(
            if (playerState == Playing) 125f else 136f,
            tween(animationTime)
        ),
        y3 = animateFloatAsState(
            if (playerState == Playing) 133.76f else 120f,
            tween(animationTime)
        ),
        x4 = animateFloatAsState(
            if (playerState == Playing) 111f else 103f,
            tween(animationTime)
        ),
        y4 = animateFloatAsState(
            if (playerState == Playing) 133.76f else 120f,
            tween(animationTime)
        )
    )

    val rotation by animateFloatAsState(
        targetValue = if (playerState == Playing) 0f else 90f,
        animationSpec = tween(animationTime)
    )

    val path1 = Path().apply {
        moveTo(firstStick.x1.value, firstStick.y1.value)
        lineTo(firstStick.x2.value, firstStick.y2.value)
        lineTo(firstStick.x3.value, firstStick.y3.value)
        lineTo(firstStick.x4.value, firstStick.y4.value)
        close()
    }

    val path2 = Path().apply {
        moveTo(secondStick.x1.value, secondStick.y1.value)
        lineTo(secondStick.x2.value, secondStick.y2.value)
        lineTo(secondStick.x3.value, secondStick.y3.value)
        lineTo(secondStick.x4.value, secondStick.y4.value)
        close()
    }

    Canvas(
        modifier = Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .rotate(rotation)
            .clickable(onClick = onButtonClick)
    ) {
        drawPath(
            path = path1,
            color = iconColor
        )
        drawPath(
            path = path2,
            color = iconColor
        )
    }
}

class ButtonPoints(
    var x1: State<Float>,
    var y1: State<Float>,
    var x2: State<Float>,
    var y2: State<Float>,
    var x3: State<Float>,
    var y3: State<Float>,
    var x4: State<Float>,
    var y4: State<Float>
)