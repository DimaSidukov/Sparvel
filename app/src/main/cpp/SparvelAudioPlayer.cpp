//
// Created by sidukov on 05.11.2022.
//

#include "include/SparvelAudioPlayer.h"

// articles and samples to refer to:
// https://github.com/google/oboe/blob/main/docs/GettingStarted.md
// https://chromium.googlesource.com/external/github.com/google/oboe/+/refs/tags/1.1.1/docs/FullGuide.md
// https://github.com/google/oboe/tree/main/samples/RhythmGame

bool SparvelAudioPlayer::init()
{
    std::lock_guard <std::mutex> lock(_lock);
    oboe::AudioStreamBuilder builder;

    oboe::Result result = builder.setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::PowerSaving)
            // add a method for figuring out channel count (it might be that audio has only one)
            ->setChannelCount(kChannelCount)
            ->setSampleRate(kSampleRate)
            ->setSampleRateConversionQuality(oboe::SampleRateConversionQuality::Medium)
            ->setFormat(oboe::AudioFormat::Float)
            ->setDataCallback(this)
            ->openStream(_stream);

    if (result != oboe::Result::OK)
    {
        // LOGE("Failed to create stream. Error: %s", oboe::convertToText(result));
        return false;
    }

    return true;
}

void SparvelAudioPlayer::play_audio()
{
    init();
    _stream->requestStart();
}

void SparvelAudioPlayer::pause_audio()
{
}

void SparvelAudioPlayer::finish_process()
{
}

oboe::DataCallbackResult SparvelAudioPlayer::onAudioReady(oboe::AudioStream* oboeStream, void* audioData, int32_t numFrames)
{
    return oboe::DataCallbackResult();
}
