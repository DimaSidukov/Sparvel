package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.model.MusicCollection
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun CollectionSection(
    modifier: Modifier = Modifier,
    sectionName: String,
    itemList: List<MusicCollection>,
    onSectionNameClicked: () -> Unit,
    onItemClicked: () -> Unit,
    lastItem: @Composable LazyItemScope.() -> Unit = { },
) {
    SectionName(
        sectionName = sectionName,
        onItemClicked = onSectionNameClicked,
        modifier = modifier
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(
            items = itemList,
            contentType = { it }
        ) { item ->
            CollectionItem(
                playlistName = item.title,
                playlistImage = item.coverId,
                onItemClicked = onItemClicked
            )
        }
        item {
            lastItem()
        }
    }
}

@Composable
fun SectionName(
    sectionName: String,
    modifier: Modifier = Modifier,
    isClickEnabled: Boolean = true,
    onItemClicked: () -> Unit = { }
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(enabled = isClickEnabled, onClick = onItemClicked)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Text(
                text = sectionName,
                style = SparvelTheme.typography.collectionTitleLarge,
                color = SparvelTheme.colors.text,
            )
        }
    }
}