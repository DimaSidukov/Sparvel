package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun TrackList(
    modifier: Modifier = Modifier,
    itemList: List<Track>,
    onItemClicked: () -> Unit = { },
    additionalContent: @Composable () -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            additionalContent()
        }
        items(itemList) { item ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            color = SparvelTheme.colors.cursor
                        )
                    ) { onItemClicked() },
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncOrPlaceholderImage(
                    imageUrl = item.coverId,
                    imageSize = 50,
                    needGradient = false
                )
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        item.title,
                        style = SparvelTheme.typography.trackTitleSmall,
                        color = SparvelTheme.colors.secondary
                    )
                    Text(
                        text = stringResource(
                            R.string.composer_album_label,
                            item.composer,
                            item.album
                        ),
                        style = SparvelTheme.typography.collectionTitleSmall,
                        color = SparvelTheme.colors.secondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}