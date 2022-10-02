package com.sidukov.sparvel.features.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.theme.SparvelTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CollapsableView(
    content: @Composable ColumnScope.() -> Unit
) {

    val minHeightSize = 70.dp
    val animationDpValue = 40.dp
    val animationDelay = 5L

    var height by remember {
        mutableStateOf(minHeightSize)
    }

    var startHeight by remember { mutableStateOf(0.dp) }
    var endHeight by remember { mutableStateOf(0.dp) }

    var isTouchEnabled by remember {
        mutableStateOf(true)
    }

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp + WindowInsets.systemBars.getTop(
            LocalDensity.current
        ).dp
    val scope = rememberCoroutineScope()

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
                        scope.launch {
                            while (height < screenHeight) {
                                height += if (screenHeight - height >= animationDpValue) animationDpValue else screenHeight - height
                                delay(animationDelay)
                            }
                            endHeight = height.also { startHeight = it }
                            isTouchEnabled = true
                        }
                    }
                }
                .draggable(
                    enabled = isTouchEnabled,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            if (delta < 0 && height - delta.dp <= screenHeight || delta > 0 && height - delta.dp >= minHeightSize)
                                height -= delta.dp
                        }
                    },
                    orientation = Orientation.Vertical,
                    onDragStarted = {
                        startHeight = height
                    },
                    onDragStopped = {
                        endHeight = height
                        if (endHeight >= startHeight) {
                            scope.launch {
                                while (height < screenHeight) {
                                    height += if (screenHeight - height >= animationDpValue) animationDpValue else screenHeight - height
                                    delay(animationDelay)
                                }
                            }
                        } else {
                            scope.launch {
                                while (height > minHeightSize) {
                                    height -= if (height - minHeightSize >= animationDpValue) animationDpValue else height - minHeightSize
                                    delay(animationDelay)
                                }
                            }
                        }
                    }
                ),
            content = content
        )
    }
}