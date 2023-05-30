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

class AudioPlayer : public oboe::AudioStreamCallback {

private:
    float *data;
    size_t size;
    int sampleRate;
    int defaultSampleRate;
    int channelCount;
    std::atomic<size_t> currentFrame;
    std::shared_ptr<oboe::AudioStream> audioStream;

    void init();

    // maybe it is possible to implement via interface
    // the same approach as oboe::AudioStreamCallback
    void (*onPositionUpdatedCallback)(int64_t position);

public:
    AudioPlayer(DecodedData *decodedData, void (*callback)(int64_t position)) :
            data(std::move(decodedData->data)),
            size(decodedData->size),
            sampleRate(decodedData->sampleRate),
            channelCount(decodedData->channelCount),
            onPositionUpdatedCallback(callback) {
        init();
    }

    ~AudioPlayer() {
        audioStream->requestStop();
        data = nullptr;
        onPositionUpdatedCallback(0);
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

    void pause();

    void stop();

    void seek(int64_t position);

};
