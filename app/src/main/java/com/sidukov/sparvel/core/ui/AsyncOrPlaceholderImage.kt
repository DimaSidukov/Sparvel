package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.applyIf
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun AsyncOrPlaceholderImage(
    imageUrl: String,
    imageSize: Int,
    needGradient: Boolean,
    onImageClicked: () -> Unit = { }
) {
    var dynamicImageSize by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, SparvelTheme.colors.background),
        startY = dynamicImageSize.height * 0.3f,
        endY = dynamicImageSize.height * 0.9f
    )

    Box(
        modifier = Modifier.size(imageSize.dp)
    ) {
        var isImageLoaded by remember { mutableStateOf(false) }
        if (!isImageLoaded) {
            Image(
                painter = painterResource(R.drawable.ic_melody),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SparvelTheme.colors.background)
                    .border(
                        1.dp,
                        SparvelTheme.colors.textPlaceholder,
                        RoundedCornerShape(10.dp)
                    )
                    .padding((0.3 * imageSize).dp)
                    .size((0.4 * imageSize).dp),
                colorFilter = ColorFilter.tint(SparvelTheme.colors.secondary)
            )
        }
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            onSuccess = { isImageLoaded = true },
            modifier = Modifier
                .size(imageSize.dp)
                .onGloballyPositioned { dynamicImageSize = it.size }
                .applyIf(needGradient) {
                    drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.SrcOver)
                        }
                    }
                }
                .clip(RoundedCornerShape(10.dp))
                .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp))
                .clickable { onImageClicked() }
        )
    }
}