package com.sidukov.sparvel.features.player

import androidx.compose.runtime.Composable
import com.sidukov.sparvel.core.model.Track
import com.sidukov.sparvel.core.ui.HQImageOrPlaceholder

@Composable
fun PlayerView(track: Track) {

    CollapsableView {
        HQImageOrPlaceholder(
            imageUrl = track.coverId,
            imageSize = 500,
            needGradient = true
        )
    }
}
