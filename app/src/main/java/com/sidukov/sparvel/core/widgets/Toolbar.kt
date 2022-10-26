package com.sidukov.sparvel.core.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.systemBarsPadding
import com.sidukov.sparvel.core.theme.SparvelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    @DrawableRes navigationIcon: Int,
    @DrawableRes actionIcon: Int,
    title: String? = null,
    isEnabled: Boolean = true,
    onNavigationClicked: () -> Unit,
    onActionClicked: () -> Unit,
) = CenterAlignedTopAppBar(
    modifier = Modifier
        .systemBarsPadding(),
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent
    ),
    title = {
        title?.let {
            Text(
                text = title,
                style = SparvelTheme.typography.toolbarTitle,
                color = SparvelTheme.colors.toolbarTitle
            )
        }
    },
    navigationIcon = {
        IconButton(
            modifier = Modifier.padding(start = 15.dp),
            enabled = isEnabled,
            onClick = onNavigationClicked
        ) {
            Icon(
                painter = painterResource(navigationIcon),
                contentDescription = null,
                tint = SparvelTheme.colors.navigation
            )
        }
    },
    actions = {
        IconButton(
            modifier = Modifier.padding(end = 25.dp),
            enabled = isEnabled,
            onClick = onActionClicked
        ) {
            Icon(
                painter = painterResource(actionIcon),
                contentDescription = null,
                tint = SparvelTheme.colors.navigation
            )
        }
    }
)