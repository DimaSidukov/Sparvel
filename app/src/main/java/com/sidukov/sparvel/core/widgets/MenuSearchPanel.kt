package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 30.dp, bottom = 30.dp, top = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = null,
                tint = SparvelTheme.colors.navigation,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            radius = 25.dp
                        ),
                        onClick = onMenuClicked
                    )
                    .size(50.dp)
                    .padding(13.dp)
            )
            SearchBar(modifier = Modifier.padding(start = 15.dp)) {
                onTextUpdated(it)
            }
        }
        content()
    }
}