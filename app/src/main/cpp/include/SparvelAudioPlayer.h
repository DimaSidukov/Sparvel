//
// Created by sidukov on 05.11.2022.
//

#ifndef SPARVEL_SPARVELAUDIOPLAYER_H
#define SPARVEL_SPARVELAUDIOPLAYER_H

#include <oboe/Oboe.h>
#include "AudioPlayer.h"
#include <android/log.h>
#include <math.h>
#include <memory>

class SparvelAudioPlayer :
        public AudioPlayer,
        public oboe::AudioStreamDataCallback {
private:
    std::shared_ptr <oboe::AudioStream> _stream;
    std::mutex _lock;
    int _sampleRate = 48000;
    int _framesPerBurst;
    // Wave params, these could be instance variables in order to modify at runtime
    static float constexpr kAmplitude = 0.5f;
    static float constexpr kFrequency = 440;
    static float constexpr kPI = 3.14;
    static float constexpr kTwoPi = kPI * 2;
    // static double constexpr mPhaseIncrement = kFrequency * kTwoPi / (double)_sampleRate;
    // Keeps track of where the wave is
    float mPhase = 0.0;

public:
    ~SparvelAudioPlayer() = default;
    SparvelAudioPlayer(int32_t defaultSampleRate, int32_t defaultFramesPerBurst);

    bool init();

    void play_audio();

    void pause_audio();

    void finish_process();

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream* oboeStream, void* audioData, int32_t numFrames);
};

#endif //SPARVEL_SPARVELAUDIOPLAYER_H
