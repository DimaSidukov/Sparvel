package com.sidukov.sparvel.features.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.theme.SparvelTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CollapsableView(
    screenHeight: Dp,
    minHeight: Dp,
    shouldCollapseView: Boolean,
    content: @Composable ColumnScope.(
        currentHeight: Dp
    ) -> Unit
) {

    val animationDpValue = 100.dp
    val animationDelay = 5L

    var startHeight by remember { mutableStateOf(0.dp) }
    var endHeight by remember { mutableStateOf(0.dp) }

    var height by remember {
        mutableStateOf(minHeight)
    }

    var isTouchEnabled by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()

    fun expandView(actionOnFinish: () -> Unit = { }) = scope.launch {
        while (height < screenHeight) {
            height += if (screenHeight - height >= animationDpValue) animationDpValue else screenHeight - height
            delay(animationDelay)
        }
        actionOnFinish()
    }

    fun collapseView(actionOnFinish: () -> Unit = { }) = scope.launch {
        while (height > minHeight) {
            height -= if (height - minHeight >= animationDpValue) animationDpValue else height - minHeight
            delay(animationDelay)
        }
        actionOnFinish()
    }

    if (shouldCollapseView) collapseView()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .align(Alignment.BottomCenter)
                .background(SparvelTheme.colors.playerBackground)
                .clickable(
                    enabled = isTouchEnabled
                ) {
                    isTouchEnabled = false
                    if (height < screenHeight) {
                        expandView {
                            endHeight = height.also { startHeight = it }
                            isTouchEnabled = true
                        }
                    }
                }
                .draggable(
                    enabled = isTouchEnabled,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            if (delta < 0 && height - delta.dp <= screenHeight || delta > 0 && height - delta.dp >= minHeight)
                                height -= delta.dp
                        }
                    },
                    orientation = Orientation.Vertical,
                    onDragStarted = {
                        startHeight = height
                    },
                    onDragStopped = {
                        endHeight = height
                        if (endHeight >= startHeight) expandView() else collapseView()
                    }
                ),
            content = {
                content(height)
            }
        )
    }
}