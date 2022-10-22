package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun CollectionScreen(
    modifier: Modifier = Modifier,
    sectionName: String,
    collectionList: List<MusicCollection>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        val contentWidth = 150
        SectionName(sectionName)
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            items(collectionList) { collection ->
                Column {
                    ImageOrPlaceholder(
                        imageUrl = collection.coverId,
                        imageSize = contentWidth,
                        needGradient = true
                    )
                    Text(
                        modifier = Modifier
                            .width(contentWidth.dp)
                            .offset(y = (-10).dp),
                        text = collection.title,
                        textAlign = TextAlign.Center,
                        color = SparvelTheme.colors.secondary,
                        overflow = TextOverflow.Ellipsis,
                        style = SparvelTheme.typography.collectionTitleMedium,
                        maxLines = 1
                    )
                    val tracksAmount = stringResource(R.string.album_info_label, collection.tracks.size)
                    Text(
                        modifier = Modifier.width(contentWidth.dp),
                        text = if (collection.year != -1) "$tracksAmount • ${collection.year}" else tracksAmount,
                        textAlign = TextAlign.Center,
                        color = SparvelTheme.colors.secondary,
                        style = SparvelTheme.typography.collectionInfo,
                        maxLines = 1
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}