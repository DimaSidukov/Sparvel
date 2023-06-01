package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.applyGradient
import com.sidukov.sparvel.core.functionality.applyIf
import com.sidukov.sparvel.core.theme.SparvelTheme

sealed class ImageType {
    object Borderless : ImageType()
    object Boxed : ImageType()
}

@Composable
fun SparvelImage(
    imageUri: String,
    imageSize: Int,
    needGradient: Boolean,
    imageType: ImageType = ImageType.Boxed,
    onImageClicked: (() -> Unit)? = null
) {
    var dynamicImageSize by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(SparvelTheme.colors.background, Color.Transparent),
        startY = dynamicImageSize.height * 0.6f,
        endY = dynamicImageSize.height * 0.9f
    )

    val isBoxed = imageType == ImageType.Boxed

    Box(
        modifier = Modifier.applyIf(isBoxed) {
            size(imageSize.dp).clip(RoundedCornerShape(10.dp))
        }
    ) {
        Box(
            modifier = Modifier
                .applyIf(isBoxed) {
                    fillMaxSize()
                        .background(gradient)
                        .applyGradient(needGradient, gradient)
                        .border(
                            1.dp,
                            SparvelTheme.colors.textPlaceholder,
                            RoundedCornerShape(10.dp)
                        )
                }
                .applyIf(onImageClicked != null) {
                    clickable(onClick = onImageClicked!!)
                }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_melody),
                contentDescription = null,
                modifier = Modifier
                    .applyIf(isBoxed) {
                        align(Alignment.Center).size((0.4 * imageSize).dp)
                    }
                    .applyIf(!isBoxed) {
                        padding((0.3 * imageSize).dp).size((0.4 * imageSize).dp)
                    },
                colorFilter = ColorFilter.tint(SparvelTheme.colors.secondary)
            )
        }
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .applyIf(isBoxed) { size(imageSize.dp) }
                .applyIf(!isBoxed) { width(imageSize.dp) }
                .onGloballyPositioned { dynamicImageSize = it.size }
                .applyGradient(needGradient, gradient)
                .applyIf(onImageClicked != null) {
                    clickable(onClick = onImageClicked!!)
                }
        )
    }
}