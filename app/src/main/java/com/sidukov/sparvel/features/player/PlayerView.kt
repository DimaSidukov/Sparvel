package com.sidukov.sparvel.features.player

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlayerView() {

    val minHeightSize = 80.dp
    val animationDpValue = 20.dp

    var height by remember {
        mutableStateOf(minHeightSize)
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .align(Alignment.BottomCenter)
                .background(Color.Red)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            val newValue = height - (dragAmount.y * 2.5)
                                .toInt()
                                .toDp()
                            if (dragAmount.y < 0 && newValue <= screenHeight) {
                                height -= (dragAmount.y * 2.5)
                                    .toInt()
                                    .toDp()
                            } else if (dragAmount.y > 0 && newValue >= minHeightSize) {
                                height -= (dragAmount.y * 2.5)
                                    .toInt()
                                    .toDp()
                            }
                        },
                        onDragEnd = {
                            if (height in (screenHeight / 2)..screenHeight) {
                                scope.launch {
                                    while (height < screenHeight) {
                                        height += if (screenHeight - height >= animationDpValue) {
                                            animationDpValue
                                        } else {
                                            screenHeight - height

                                        }
                                        delay(5)
                                    }
                                }
                            } else {
                                scope.launch {
                                    while (height > minHeightSize) {
                                        height -= if (height - minHeightSize >= animationDpValue) {
                                            animationDpValue
                                        } else {
                                            height - minHeightSize
                                        }
                                        delay(5)
                                    }
                                }
                            }
                        }
                    )
                }
        )
    }
}