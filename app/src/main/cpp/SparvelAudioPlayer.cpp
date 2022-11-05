//
// Created by sidukov on 05.11.2022.
//

#include "include/SparvelAudioPlayer.h"

// articles and samples to refer to:
//  https://github.com/google/oboe/blob/main/docs/GettingStarted.md
//  https://chromium.googlesource.com/external/github.com/google/oboe/+/refs/tags/1.1.1/docs/FullGuide.md
// https://github.com/google/oboe/tree/main/samples/RhythmGame

void SparvelAudioPlayer::init() {
    std::lock_guard <std::mutex> lock(_lock);
    oboe::AudioStreamBuilder builder;

    Result result = builder.setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::PowerSaving)
}

void SparvelAudioPlayer::play_audio() {

}

void SparvelAudioPlayer::pause_audio() {

}

void SparvelAudioPlayer::finish_process() {

}

oboe::DataCallbackResult
SparvelAudioPlayer::onAudioReady(oboe::AudioStream *oboeStream, void *audioData,
                                 int32_t numFrames) {
    return oboe::DataCallbackResult::Continue;
}
