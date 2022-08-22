package com.sidukov.sparvel.core.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.theme.SparvelTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollectionSection(
    modifier: Modifier = Modifier,
    sectionName: String,
    itemList: List<String>,
    onItemClicked: () -> Unit,
    lastItem: @Composable LazyItemScope.() -> Unit = { },
) {
    Text(
        modifier = modifier,
        text = sectionName,
        style = SparvelTheme.typography.collectionTitleLarge,
        color = SparvelTheme.colors.secondary
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(itemList) { item ->
            CollectionItem(
                playlistName = item,
                // temporary measure
                playlistImage = painterResource(R.drawable.ic_launcher_background),
            ) {
                onItemClicked()
            }
        }
        item {
            lastItem()
        }
    }
}