package com.sidukov.sparvel.core.model

import android.graphics.Bitmap

class MusicCollection(
    val title: String,
    val cover: Bitmap?,
    val composer: String? = null,
    val tracks: List<Track>
)