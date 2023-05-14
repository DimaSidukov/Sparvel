package com.sidukov.audiomanager

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER
import android.media.AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE
import android.widget.Toast
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AudioManager(private val context: Context) {

    // pass these to oboe and set as default
    private var defaultSampleRate: Int
    private var defaultFramesPerBurst: Int
    private var songLength: Long = 0L
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentPosition: MutableSharedFlow<Float> = MutableSharedFlow(replay = 1)
    val currentPosition = _currentPosition.asSharedFlow()

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

    fun play(uri: String, length: Long) {
        songLength = length
        coroutineScope.launch {
            nativePlay(uri)
        }
    }

    fun pause() = nativePause()

    fun finish() = nativeFinish()

    fun seek(position: Float) = nativeSeek((position * songLength).toLong())

    private fun showToast(message: String) {
        coroutineScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPositionUpdated(position: Long) {
        coroutineScope.launch {
            _currentPosition.emit(
                if (songLength == 0L) 0f else (position.toFloat() / songLength.toFloat())
            )
        }
    }

    private external fun nativePlay(
        filePath: String,
        defaultSampleRate: Int = this.defaultSampleRate,
        defaultFramesPerBurst: Int = this.defaultFramesPerBurst
    )

    private external fun nativePause()

    private external fun nativeFinish()

    private external fun nativeSeek(position: Long)

}