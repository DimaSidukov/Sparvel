package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun AddPlaylistItem(
    size: DpSize = DpSize(100.dp, 100.dp),
    onCreateClicked: () -> Unit
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(50f, 12.5f), 0f)
    val borderColor = SparvelTheme.colors.textPlaceholder

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(size)
                .background(Color.Transparent)
                .clickable {
                    onCreateClicked()
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = borderColor,
                    style = Stroke(width = 3f, pathEffect = pathEffect),
                    cornerRadius = CornerRadius(20f)
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                alignment = Alignment.Center,
            )
        }

        Text(
            modifier = Modifier.width(size.width).padding(top = 10.dp),
            text = stringResource(R.string.add_new_playlist),
            style = SparvelTheme.typography.collectionTitleSmall,
            textAlign = TextAlign.Center,
            color = SparvelTheme.colors.secondary
        )
    }
}