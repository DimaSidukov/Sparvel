package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.applyIf
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun CollectionItem(
    playlistName: String,
    playlistImage: Painter?,
    needGradient: Boolean = false,
    onItemClicked: () -> Unit
) {

    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, SparvelTheme.colors.background),
        startY = imageSize.height * 0.3f,
        endY = imageSize.height * 0.9f
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        playlistImage?.let { image ->
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .onGloballyPositioned { imageSize = it.size }
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
                    .clickable { onItemClicked() }
            )
        }

        Text(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 10.dp),
            text = playlistName,
            style = SparvelTheme.typography.collectionTitleSmall,
            textAlign = TextAlign.Center,
            color = SparvelTheme.colors.secondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}