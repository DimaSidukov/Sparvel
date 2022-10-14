package com.sidukov.sparvel.features.player

import android.util.Log
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

    val animationDpValue = 20.dp
    val animationDelay = 1L

    var height by remember {
        mutableStateOf(minHeight)
    }

    // used to calculate whether view should be collapsed or expanded
    var startHeight by remember { mutableStateOf(0.dp) }
    var endHeight by remember { mutableStateOf(0.dp) }

    var isTouchEnabled by remember { mutableStateOf(true) }

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

    LaunchedEffect(Unit) {
        if (shouldCollapseView) {
            collapseView()
        }
    }

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
                    enabled = isTouchEnabled,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isTouchEnabled = false
                    if (height < screenHeight) {
                        expandView {
                            isTouchEnabled = true
                        }
                    }
                }
                .draggable(
                    enabled = isTouchEnabled,
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            if (delta < 0 && height - delta.dp <= screenHeight || delta > 0 && height - delta.dp >= minHeight) {
                                height -= delta.dp
                            }
                        }
                    },
                    onDragStarted = {
                        startHeight = height
                        Log.d("HEIGHTS", "${startHeight} ${endHeight}")
                    },
                    onDragStopped = {
                        endHeight = height
                        Log.d("HEIGHTS", "${startHeight} ${endHeight}")
                        if (endHeight > startHeight) expandView() else collapseView()
                    }
                ),
            content = {
                content(height)
            }
        )
    }
}