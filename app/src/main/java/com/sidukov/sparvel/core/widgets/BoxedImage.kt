package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.applyGradient
import com.sidukov.sparvel.core.functionality.applyIf
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun BoxedImage(
    imageUri: String,
    imageSize: Int,
    needGradient: Boolean,
    onImageClicked: (() -> Unit)? = null
) {
    var dynamicImageSize by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(SparvelTheme.colors.background, Color.Transparent),
        startY = dynamicImageSize.height * 0.3f,
        endY = dynamicImageSize.height * 0.9f
    )

    Box(
        modifier = Modifier
            .size(imageSize.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        var isImageLoaded by remember { mutableStateOf(false) }
        if (!isImageLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient)
                    .applyGradient(needGradient, gradient)
                    .border(1.dp, SparvelTheme.colors.textPlaceholder, RoundedCornerShape(10.dp))
                    .applyIf(onImageClicked != null) {
                        clickable(onClick = onImageClicked!!)
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_melody),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size((0.4 * imageSize).dp),
                    colorFilter = ColorFilter.tint(SparvelTheme.colors.secondary)
                )
            }
        }
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            onSuccess = { isImageLoaded = true },
            onError = { isImageLoaded = false },
            modifier = Modifier
                .size(imageSize.dp)
                .onGloballyPositioned { dynamicImageSize = it.size }
                .applyGradient(needGradient, gradient)
                .applyIf(onImageClicked != null) {
                    clickable(onClick = onImageClicked!!)
                }
        )

    }
}