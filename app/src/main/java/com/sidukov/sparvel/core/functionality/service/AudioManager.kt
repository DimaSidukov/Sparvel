package com.sidukov.sparvel.core.functionality.service

import android.content.Context
import androidx.core.content.getSystemService
import android.media.AudioManager as AndroidAudioManager

class AudioManager(private val context: Context) {

    private var defaultSampleRate = -1
    private var defaultFramesPerBurst = -1

    companion object {
        init {
            // System.loadLibrary("audioplayer")
        }
    }

    init {
        val am = context.getSystemService<AndroidAudioManager>()
        defaultSampleRate = am?.getProperty(
            AndroidAudioManager.PROPERTY_OUTPUT_SAMPLE_RATE
        )?.toInt() ?: -1
        defaultFramesPerBurst = am?.getProperty(
            AndroidAudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER
        )?.toInt() ?: -1
    }

//    external fun play(
//        fullPath: String,
//        defaultSampleRate: Int = this.defaultSampleRate,
//        defaultFramesPerBurst: Int = this.defaultFramesPerBurst
//    )
//
//    external fun pause()
//
//    external fun finish()

}