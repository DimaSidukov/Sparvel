package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.applyGradient
import com.sidukov.sparvel.core.functionality.applyIf
import com.sidukov.sparvel.core.functionality.decodeBitmap
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun HQImageOrPlaceholder(
    imageUrl: String,
    imageSize: Int,
    needGradient: Boolean,
    alpha: Float,
    onImageClicked: (() -> Unit)? = null
) {
    var dynamicImageSize by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(SparvelTheme.colors.background, Color.Transparent),
        startY = dynamicImageSize.height * 0.3f,
        endY = dynamicImageSize.height * 0.9f
    )

    val bitmap = imageUrl.decodeBitmap()

    Box(
        modifier = Modifier
            .size(imageSize.dp)
    ) {
        if (bitmap == null) {
            Image(
                painter = painterResource(R.drawable.ic_melody),
                contentDescription = null,
                modifier = Modifier
                    .background(gradient)
                    .alpha(alpha)
                    .padding((0.3 * imageSize).dp)
                    .size((0.4 * imageSize).dp)
                    .applyIf(onImageClicked != null) {
                        clickable(onClick = onImageClicked!!)
                    },
                colorFilter = ColorFilter.tint(SparvelTheme.colors.secondary)
            )
        } else {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(imageSize.dp)
                    .alpha(alpha)
                    .onGloballyPositioned { dynamicImageSize = it.size }
                    .applyGradient(needGradient, gradient)
                    .applyIf(onImageClicked != null) {
                        clickable(onClick = onImageClicked!!)
                    }
            )
        }
    }
}