package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.model.TrackItem
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun TrackList(
    modifier: Modifier = Modifier,
    itemList: List<TrackItem>,
    onItemClicked: () -> Unit = { },
    additionalContent: @Composable () -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        item {
            additionalContent()
        }
        items(itemList) { item ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onItemClicked() },
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp)),
                    // temporary measure
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null
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
                        stringResource(R.string.composer_album_label, item.composer, item.album),
                        style = SparvelTheme.typography.collectionTitleSmall,
                        color = SparvelTheme.colors.secondary
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