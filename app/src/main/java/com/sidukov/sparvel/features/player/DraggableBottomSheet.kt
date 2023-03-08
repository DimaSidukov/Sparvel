package com.sidukov.sparvel.features.player

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.theme.SparvelTheme
import kotlinx.coroutines.launch

@Composable
fun DraggableBottomSheet(
    screenHeight: Dp,
    minHeight: Dp,
    shouldMoveDown: Boolean,
    content: @Composable BoxScope.(
        currentHeight: Dp,
        isExpanded: Boolean
    ) -> Unit
) {

    var startPosition by remember { mutableStateOf(0.dp) }
    var endPosition by remember { mutableStateOf(0.dp) }

    val touchThreshold = 150.dp

    var isExpanded by remember { mutableStateOf(false) }
    var height by remember { mutableStateOf(minHeight) }

    val animationDuration = 250

    val scope = rememberCoroutineScope()

    fun collapse() {
        scope.launch {
            animate(
                initialValue = height.value,
                targetValue = minHeight.value,
                animationSpec = tween(animationDuration),
                block = { value, _ ->
                    height = Dp(value)
                }
            )
            isExpanded = false
        }
    }

    fun expand() {
        scope.launch {
            animate(
                initialValue = height.value,
                targetValue = screenHeight.value,
                animationSpec = tween(animationDuration),
                block = { value, _ ->
                    height = Dp(value)
                }
            )
            isExpanded = true
        }
    }

    SideEffect {
        if (shouldMoveDown) {
            collapse()
        }
    }

    val gradientAnimationDuration = 20000
    val gradientStartX = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx() / 1.4f
    }
    val gradientStartY = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx() / 1.3f
    }
    val gradientX by rememberInfiniteTransition().animateFloat(
        initialValue = gradientStartX,
        targetValue = gradientStartX,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = gradientAnimationDuration
                gradientStartX at 0 with FastOutSlowInEasing
                gradientStartX + 500f at gradientAnimationDuration / 4 with FastOutSlowInEasing
                gradientStartX at gradientAnimationDuration / 3 with FastOutSlowInEasing
                gradientStartX - 500f at gradientAnimationDuration / 2 with FastOutSlowInEasing
                gradientStartX at gradientAnimationDuration with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val gradientY by rememberInfiniteTransition().animateFloat(
        initialValue = gradientStartY,
        targetValue = gradientStartY,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = gradientAnimationDuration
                gradientStartY at 0 with FastOutSlowInEasing
                gradientStartY - 300f at gradientAnimationDuration / 4 with FastOutSlowInEasing
                gradientStartY at gradientAnimationDuration / 3 with FastOutSlowInEasing
                gradientStartY + 300f at gradientAnimationDuration / 2 with FastOutSlowInEasing
                gradientStartY at gradientAnimationDuration with FastOutSlowInEasing
            }
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = screenHeight - height)
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(5, 5)
                )
                .shadow(10.dp, clip = false)
                .draggable(
                    enabled = true,
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        if (height in minHeight..screenHeight && (height - delta.dp) in minHeight..screenHeight) {
                            height -= delta.dp
                        }
                    },
                    onDragStarted = {
                        startPosition = height
                    },
                    onDragStopped = {
                        endPosition = height
                        if (endPosition > startPosition) {
                            if (endPosition - startPosition > touchThreshold) expand() else collapse()
                        } else {
                            if (startPosition - endPosition > touchThreshold) collapse() else expand()
                        }
                    }
                ),
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SparvelTheme.colors.background)
                        .blur(150.dp)
                        .background(
                            Brush.sweepGradient(
                                SparvelTheme.colors.playerBackground,
                                center = Offset(gradientX, gradientY)
                            )
                        )
                )
                content(height, isExpanded)
                if (!isExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.8f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    expand()
                                }
                            )
                    )
                }
            }
        )
    }
}