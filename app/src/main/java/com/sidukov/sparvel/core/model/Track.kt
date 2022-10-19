package com.sidukov.sparvel.core.model

import java.io.Serializable

class Track(
    val id: String,
    val track: String,
    val name: String,
    val artist: String,
    val album: String,
    var coverId: String,
    val duration: Int,
    val year: Int
) : Serializable