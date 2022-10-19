package com.sidukov.sparvel.features.player

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun DraggableView(
    screenHeight: Dp,
    minHeight: Dp,
    shouldMoveDown: Boolean,
    content: @Composable ColumnScope.(
        currentHeight: Dp,
        isLayoutExpanded: Boolean
    ) -> Unit
) {

    val animationDpValue = 20.dp
    val animationDelay = 1L

    var offset by remember { mutableStateOf(minHeight) }
    var startPosition by remember { mutableStateOf(0.dp) }
    var endPosition by remember { mutableStateOf(0.dp) }

    var isLayoutExpanded by remember { mutableStateOf(false) }
    var isTouchEnabled by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    fun moveUp(actionOnFinish: () -> Unit = { }) = scope.launch {
        while (offset < screenHeight) {
            offset += if (screenHeight - offset >= animationDpValue) animationDpValue else screenHeight - offset
            delay(animationDelay)
        }
        actionOnFinish()
        isTouchEnabled = true
        isLayoutExpanded = true
    }

    fun moveDown(actionOnFinish: () -> Unit = { }) = scope.launch {
        while (offset > minHeight) {
            offset -= if (offset - minHeight >= animationDpValue) animationDpValue else offset - minHeight
            delay(animationDelay)
        }
        actionOnFinish()
        isTouchEnabled = true
        isLayoutExpanded = false
    }

    SideEffect {
        if (shouldMoveDown) {
            moveDown()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = screenHeight - offset)
                .align(Alignment.BottomCenter)
                .background(SparvelTheme.colors.playerBackground)
                .clickable(
                    enabled = isTouchEnabled,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isTouchEnabled = false
                    if (offset < screenHeight) {
                        moveUp()
                    }
                }
                .draggable(
                    enabled = isTouchEnabled,
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            if (delta < 0 && offset - delta.dp <= screenHeight || delta > 0 && offset - delta.dp >= minHeight) {
                                offset -= delta.dp
                            }
                        }
                    },
                    onDragStarted = {
                        startPosition = offset
                    },
                    onDragStopped = {
                        endPosition = offset
                        if (endPosition >= startPosition) {
                            if (abs(endPosition.value - startPosition.value) > 100f) {
                                moveUp()
                            } else {
                                moveDown()
                            }
                        } else if (endPosition < startPosition) {
                            if (abs(endPosition.value - startPosition.value) > 100f) {
                                moveDown()
                            } else {
                                moveUp()
                            }
                        }
                    }
                ),
            content = {
                content(offset, isLayoutExpanded)
            }
        )
    }
}