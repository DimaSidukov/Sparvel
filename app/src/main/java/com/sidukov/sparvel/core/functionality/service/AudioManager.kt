package com.sidukov.sparvel.core.functionality.service

class AudioManager {

    init {
        System.loadLibrary("sparvel")
    }

    external fun play(trackId: Int)

    external fun pause()

    external fun finish()

}