package com.sidukov.sparvel.features.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.sidukov.sparvel.core.functionality.applyGradient
import com.sidukov.sparvel.core.functionality.applyIf
import com.sidukov.sparvel.core.functionality.decodeBitmap
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.ui.HQImageOrPlaceholder

@Composable
fun PlayerView(track: Track) {

    CollapsableView { currentHeight, maxHeight ->
        HQImageOrPlaceholder(
            imageUrl = track.coverId,
            imageSize = 500,
            needGradient = true,
            currentHeight = currentHeight,
            maxHeight = maxHeight
        )
    }
}
