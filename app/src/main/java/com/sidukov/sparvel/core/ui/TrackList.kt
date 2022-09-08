package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
                    .clickable { onItemClicked() }
                    .then(modifier),
                horizontalArrangement = Arrangement.Start
            ) {
                var isImagePresent by remember { mutableStateOf(true) }
                if (isImagePresent) {
                    AsyncImage(
                        model = item.coverId,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp)),
                        contentDescription = null,
                        onError = {
                            isImagePresent = false
                        }
                    )
                } else {
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
                            .padding(15.dp)
                            .size(20.dp),
                        colorFilter = ColorFilter.tint(SparvelTheme.colors.secondary)
                    )
                }
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
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}