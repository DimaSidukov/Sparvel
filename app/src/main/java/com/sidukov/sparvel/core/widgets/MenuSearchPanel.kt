package com.sidukov.sparvel.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
        Box(
            modifier = Modifier
                .padding(end = 30.dp, bottom = 30.dp, top = 30.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .size(50.dp)
                    .align(Alignment.CenterStart)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            radius = 25.dp
                        ),
                        onClick = onMenuClicked
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center),
                    colorFilter = ColorFilter.tint(SparvelTheme.colors.navigation)
                )
            }
            SearchBar(
                modifier = Modifier
                    .padding(start = 80.dp)
                    .align(Alignment.CenterStart)
            ) {
                onTextUpdated(it)
            }
        }
        content()
    }
}