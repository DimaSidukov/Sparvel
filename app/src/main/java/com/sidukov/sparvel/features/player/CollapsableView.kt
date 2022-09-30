package com.sidukov.sparvel.features.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.background
import com.sidukov.sparvel.core.functionality.toIntToDp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CollapsableView(
    content: @Composable () -> Unit
) {

    val minHeightSize = 150.dp
    val animationDpValue = 20.dp
    val animationDelay = 5L
    val dragRatio = 1.5

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
                .background(Color.Red)
                .clickable(
                    enabled = isTouchEnabled
                ) {
                    isTouchEnabled = false
                    if (height < screenHeight) {
                        scope.launch {
                            while (height < screenHeight) {
                                height += if (screenHeight - height >= animationDpValue * 2) animationDpValue * 2 else screenHeight - height
                                delay(animationDelay)
                            }
                            endHeight = height.also { startHeight = it }
                            isTouchEnabled = true
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            startHeight = height
                        },
                        onHorizontalDrag = { _, y ->
                            val newValue = height - (y * dragRatio).toIntToDp()
                            if (y < 0 && newValue <= screenHeight || y > 0 && newValue >= minHeightSize) {
                                height -= (y * dragRatio).toIntToDp()
                            }
                            endHeight = height
                        },
                        onDragEnd = {
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
                    )
                },
        ) {
            content()
        }
    }
}