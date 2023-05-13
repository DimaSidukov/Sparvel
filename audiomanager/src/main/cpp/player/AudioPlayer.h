//
// Created by siduk on 13.05.2023.
//

#pragma once

#include <cstdio>
#include <utility>
#include <oboe/Oboe.h>
#include <oboe/AudioStream.h>
#include "DecodingUtilities.h"

static int gFramesPerCallback = 192;

class AudioPlayer: public oboe::AudioStreamCallback {

private:
    float* data;
    size_t size;
    int sampleRate;
    int channelCount;
    int currentFrame;
    std::shared_ptr<oboe::AudioStream> audioStream;

    void init();

public:
    AudioPlayer(DecodedData* decodedData) :
        data(std::move(decodedData->data)),
        size(decodedData->size),
        sampleRate(decodedData->sampleRate),
        channelCount(decodedData->channelCount) {
        init();
    }

    oboe::DataCallbackResult onAudioReady(
            oboe::AudioStream *oboeStream,
            void *audioData,
            int32_t numFrames
    ) override;

    void onErrorAfterClose(
            oboe::AudioStream *oboeStream,
            oboe::Result error
    ) override;

    void onErrorBeforeClose(oboe::AudioStream *oboeStream, oboe::Result error) override;

    bool onError(oboe::AudioStream *oboeStream, oboe::Result error) override;

    void play();

};
