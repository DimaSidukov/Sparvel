package com.sidukov.audiomanager

import android.content.Context
import android.media.AudioManager
import androidx.core.content.getSystemService

class AudioManager(context: Context) {

    private var defaultSampleRate: Int
    private var defaultFramesPerBurst: Int

    companion object {
        init {
            System.loadLibrary("audio_engine")
        }
    }

    init {
        val am = context.getSystemService<AudioManager>()
        defaultSampleRate = am?.getProperty(
            AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE
        )?.toInt() ?: -1
        defaultFramesPerBurst = am?.getProperty(
            AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER
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