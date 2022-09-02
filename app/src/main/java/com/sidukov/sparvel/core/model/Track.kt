package com.sidukov.sparvel.core.model

import android.graphics.Bitmap

class Track(
    val id: String,
    val track: String,
    val title: String,
    val composer: String,
    val album: String,
    var cover: Bitmap?,
    val duration: Long,
    val year: Int,
)