package com.sidukov.sparvel.core.functionality.service

class AudioManager {

    companion object {
        init {
            System.loadLibrary("audioplayer")
        }
    }

    external fun play(fullPath: String)

    external fun pause()

    external fun finish()

}