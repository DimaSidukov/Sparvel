package com.sidukov.sparvel.features.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.R
import com.sidukov.sparvel.core.functionality.decodeBitmap
import com.sidukov.sparvel.core.functionality.normalize
import com.sidukov.sparvel.core.functionality.systemBarsPadding
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.ui.HQImageOrPlaceholder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(track: Track) {

    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp +
                WindowInsets.systemBars.getTop(LocalDensity.current).dp
    val minHeight = 70.dp

    var shouldCollapseView by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val image = track.coverId.decodeBitmap()

    CollapsableView(
        screenHeight = screenHeight,
        minHeight = minHeight,
        shouldCollapseView = shouldCollapseView
    ) { currentHeight, isLayoutExpanded ->
        val ratio = 0.35f
        val heightNormalized = currentHeight.normalize(screenHeight, minHeight)!!
        val alpha = if (heightNormalized < ratio) 0f else heightNormalized.normalize(1f, ratio)!!

        SideEffect {
            if (isLayoutExpanded) {
                shouldCollapseView = false
            }
        }
        Box {
            HQImageOrPlaceholder(
                image = image,
                imageSize = 500,
                needGradient = true,
                alpha = alpha
            )
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .systemBarsPadding()
                    .alpha(alpha.normalize(1f, 0.5f)!!),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = { },
                navigationIcon = {
                    IconButton(
                        enabled = isLayoutExpanded,
                        onClick = {
                            scope.launch {
                                shouldCollapseView = true
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}
