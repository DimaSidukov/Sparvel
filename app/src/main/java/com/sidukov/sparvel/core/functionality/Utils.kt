package com.sidukov.sparvel.core.functionality

import androidx.compose.ui.Modifier

fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier =
    if (condition) then(modifier(this)) else this