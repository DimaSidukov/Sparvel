package com.sidukov.sparvel.core.model

import java.io.Serializable

class Track(
    val id: String,
    val track: String,
    val title: String,
    val composer: String,
    val album: String,
    var coverId: String,
    val duration: Long,
    val year: Int
) : Serializable