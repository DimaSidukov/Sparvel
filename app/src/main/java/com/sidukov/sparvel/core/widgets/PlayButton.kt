package com.sidukov.sparvel.core.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.widgets.PlayerState.Playing

@Composable
fun PlayButton(
    isPlaying: Boolean,
    color: Color,
    onButtonClick: () -> Unit
) {

    var buttonState by remember { mutableStateOf<PlayerState>(Playing) }

    val firstStick = ButtonPoints(
        x1 = animateFloatAsState(if (buttonState == Playing) 40f else 55f),
        y1 = remember { mutableStateOf(30f) },
        x2 = animateFloatAsState(if (buttonState == Playing) 50f else 55f),
        y2 = remember { mutableStateOf(30f) },
        x3 = animateFloatAsState(if (buttonState == Playing) 50f else 55f),
        y3 = remember { mutableStateOf(83.4f) },
        x4 = animateFloatAsState(if (buttonState == Playing) 40f else 30f),
        y4 = remember { mutableStateOf(83.4f) }
    )

    val secondStick = ButtonPoints(
        x1 = animateFloatAsState(if (buttonState == Playing) 60f else 55f),
        y1 = remember { mutableStateOf(30f) },
        x2 = animateFloatAsState(if (buttonState == Playing) 70f else 55f),
        y2 = remember { mutableStateOf(30f) },
        x3 = animateFloatAsState(if (buttonState == Playing) 70f else 80f),
        y3 = remember { mutableStateOf(83.4f) },
        x4 = animateFloatAsState(if (buttonState == Playing) 60f else 55f),
        y4 = remember { mutableStateOf(83.4f) }
    )

    var shouldAnimate by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (buttonState == Playing) 90f else -90f
    )

    Canvas(
        modifier = Modifier
            .size(40.dp)
            //.clip(RoundedCornerShape(50))
            .clickable {
                onButtonClick()
                buttonState = when (buttonState) {
                    PlayerState.Paused -> Playing
                    Playing -> Playing
                }
                shouldAnimate = true
            }
            .rotate(if (shouldAnimate) rotation else 0f)
    ) {

        val path1 = Path()
        path1.moveTo(firstStick.x1.value, firstStick.y1.value)
        path1.moveTo(firstStick.x2.value, firstStick.y2.value)
        path1.moveTo(firstStick.x3.value, firstStick.y3.value)
        path1.moveTo(firstStick.x4.value, firstStick.y4.value)
        path1.close()

        val path2 = Path()
        path2.moveTo(secondStick.x1.value, secondStick.y1.value)
        path2.moveTo(secondStick.x2.value, secondStick.y2.value)
        path2.moveTo(secondStick.x3.value, secondStick.y3.value)
        path2.moveTo(secondStick.x4.value, secondStick.y4.value)
        path2.close()

        drawPath(
            path = path1,
            color = color,
        )
        drawPath(
            path = path2,
            color = color
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

sealed class PlayerState {
    object Playing : PlayerState()
    object Paused : PlayerState()
}