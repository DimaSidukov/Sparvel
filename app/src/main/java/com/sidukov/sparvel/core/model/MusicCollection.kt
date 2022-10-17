package com.sidukov.sparvel.core.model

class MusicCollection(
    val title: String,
    val coverId: String,
    val artist: String? = null,
    val tracks: List<Track>,
    val year: Int = -1
)