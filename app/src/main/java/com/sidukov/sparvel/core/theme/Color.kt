package com.sidukov.sparvel.core.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Light Colors
val backgroundLight = Color(0xFFFFFFFF)

// Dark Colors
val backgroundDark = Color(0xFF000000)


class SparvelColors(
    background: Color
) {
    var background by mutableStateOf(background)
        private set

    fun copy(
        background: Color = this.background
    ): SparvelColors = SparvelColors(
        background
    )

    fun updateColorsFrom(other: SparvelColors) {
        background = other.background
    }
}

fun lightColors(
    background: Color = backgroundLight
): SparvelColors = SparvelColors(
    background = background
)

fun darkColors(
    background: Color = backgroundDark
): SparvelColors = SparvelColors(
    background = background
)