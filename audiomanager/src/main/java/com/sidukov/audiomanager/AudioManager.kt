package com.sidukov.audiomanager

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER
import android.media.AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE
import android.util.Log
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class AudioManager(private val context: Context) {

    // pass these to oboe and set as default
    private var defaultSampleRate: Int
    private var defaultFramesPerBurst: Int
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        init {
            System.loadLibrary("audio_engine")
        }
    }

    init {
        val am = context.getSystemService<AudioManager>()
        defaultSampleRate = am?.getProperty(PROPERTY_OUTPUT_SAMPLE_RATE)?.toInt() ?: 48000
        defaultFramesPerBurst = am?.getProperty(PROPERTY_OUTPUT_FRAMES_PER_BUFFER)?.toInt() ?: 192
    }

    fun play(uri: String) = coroutineScope.launch {
        nativePlay(uri)
    }

    private external fun nativePlay(
        filePath: String,
        defaultSampleRate: Int = this.defaultSampleRate,
        defaultFramesPerBurst: Int = this.defaultFramesPerBurst
    )

//    private external fun pause()
//
//    private external fun finish()

}