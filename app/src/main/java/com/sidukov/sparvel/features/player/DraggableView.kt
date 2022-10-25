package com.sidukov.sparvel.features.player

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.theme.SparvelTheme
import kotlinx.coroutines.launch

@Composable
fun DraggableView(
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

    var isExpanded by remember { mutableStateOf(false) }
    var height by remember { mutableStateOf(minHeight) }

    val animationDuration = 300

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            shadowElevation = 10.dp,
            color = SparvelTheme.colors.playerBackground,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = screenHeight - height)
                .align(Alignment.BottomCenter)
                .clickable(
                    enabled = !isExpanded,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        expand()
                    },
                )
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
                        if (endPosition > startPosition) expand() else collapse()
                    }
                ),
            content = {
                content(height, isExpanded)
            }
        )
    }
}