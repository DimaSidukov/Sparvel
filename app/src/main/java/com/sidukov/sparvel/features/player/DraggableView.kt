package com.sidukov.sparvel.features.player

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.theme.SparvelTheme

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
    val offset by animateDpAsState(
        if (isExpanded) screenHeight else minHeight,
        tween(300)
    )

    SideEffect {
        if (shouldMoveDown) {
            isExpanded = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = screenHeight - offset)
                .align(Alignment.BottomCenter)
                .background(SparvelTheme.colors.playerBackground)
                .clickable(
                    enabled = true,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { isExpanded = true }
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
                        isExpanded = endPosition > startPosition
                        height = if (endPosition > startPosition) screenHeight else minHeight
                    }
                ),
            content = {
                content(offset, isExpanded)
            }
        )
    }
}