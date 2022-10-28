package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.SelectedTrackPadding
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun TrackList(
    modifier: Modifier = Modifier,
    itemList: List<Track>,
    isTrackSelected: Boolean,
    onItemClicked: (Track) -> Unit = { },
    additionalContent: @Composable () -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            additionalContent()
        }
        items(
            items = itemList,
            contentType = { it }
        ) { item ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .then(modifier)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            color = SparvelTheme.colors.cursor
                        )
                    ) { onItemClicked(item) },
                horizontalArrangement = Arrangement.Start
            ) {
                ImageOrPlaceholder(
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
                        item.name,
                        style = SparvelTheme.typography.trackNameSmall,
                        color = SparvelTheme.colors.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(
                            R.string.artist_album_label,
                            item.artist,
                            item.album
                        ),
                        style = SparvelTheme.typography.collectionTitleSmall,
                        color = SparvelTheme.colors.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            SelectedTrackPadding(
                isTrackSelected = isTrackSelected,
                defaultPadding = 15.dp
            )
        }
    }
}