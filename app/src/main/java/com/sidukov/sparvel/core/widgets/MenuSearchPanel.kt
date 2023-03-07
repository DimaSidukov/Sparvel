package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.systemBarsPadding
import com.sidukov.sparvel.core.theme.SparvelTheme

@Composable
fun MenuSearchPanel(
    onMenuClicked: () -> Unit,
    onTextUpdated: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 30.dp, top = 30.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .clickable(
                        onClick = onMenuClicked
                    ),
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = null,
                tint = SparvelTheme.colors.navigation,
            )
            SearchBar(modifier = Modifier.padding(start = 25.dp)) {
                onTextUpdated(it)
            }
        }
        content()
    }
}