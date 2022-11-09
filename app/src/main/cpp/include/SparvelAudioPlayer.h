//
// Created by sidukov on 05.11.2022.
//

#ifndef SPARVEL_SPARVELAUDIOPLAYER_H
#define SPARVEL_SPARVELAUDIOPLAYER_H

#include <oboe/Oboe.h>
#include <android/log.h>
#include <math.h>
#include <memory>
#include <vector>
#include <utility>
#include "AudioPlayer.h"

class SparvelAudioPlayer :
        public AudioPlayer,
        public oboe::AudioStreamDataCallback {
private:
    std::shared_ptr <oboe::AudioStream> _stream;
    std::mutex _lock;
    int _sampleRate = 48000;
    int _framesPerBurst;
    // Wave params, these could be instance variables in order to modify at runtime
    static float constexpr _amplitude = 0.5f;
    static float constexpr _frequency = 440;
    static float constexpr _pi = 3.14;
    static float constexpr _twoPi = _pi * 2;
    // static double constexpr mPhaseIncrement = _frequency * _twoPi / (double)_sampleRate;
    // Keeps track of where the wave is
    float _phase = 0.0;
    std::vector<float> _userAudioData;

public:
    ~SparvelAudioPlayer() = default;
    SparvelAudioPlayer(int32_t defaultSampleRate, int32_t defaultFramesPerBurst,
                       std::vector<float> audioData);

    bool init();

    void play_audio();

    void pause_audio();

    void finish_process();

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream* oboeStream, void* audioData, int32_t numFrames);
};

#endif //SPARVEL_SPARVELAUDIOPLAYER_H
