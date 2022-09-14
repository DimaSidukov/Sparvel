package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun CollectionItem(
    playlistName: String,
    playlistImage: String,
    needGradient: Boolean = false,
    onItemClicked: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncOrPlaceholderImage(
            imageSize = 100,
            imageUrl = playlistImage,
            needGradient = needGradient,
            onImageClicked = onItemClicked
        )
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