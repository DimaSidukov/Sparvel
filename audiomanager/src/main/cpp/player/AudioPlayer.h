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
    std::unique_ptr<FFmpegDecoder> decoder;
    const char* filePath;
    int sampleRate = -1;
    int defaultSampleRate;
    int channelCount = -1;
    std::atomic<size_t> currentFrame;
    std::shared_ptr<oboe::AudioStream> audioStream;
    std::shared_ptr<AudioBuffer> audioBuffer;

    void init();

    // maybe it is possible to implement via interface
    // the same approach as oboe::AudioStreamCallback
    void (*onPositionUpdatedCallback)(int64_t position);

public:
    AudioPlayer(const char* filePath, void (*callback)(int64_t position)) :
            filePath(std::move(filePath)),
            onPositionUpdatedCallback(callback) {
        init();
    }

    ~AudioPlayer() {
        onPositionUpdatedCallback(0);
        audioStream->requestStop();
        audioBuffer->reset();
        decoder.reset();
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

    void update(const char* path);

    void onErrorBeforeClose(oboe::AudioStream *oboeStream, oboe::Result error) override;

    bool onError(oboe::AudioStream *oboeStream, oboe::Result error) override;

    void play();

    void pause();

    void stop();

    void seek(int64_t position);

};
