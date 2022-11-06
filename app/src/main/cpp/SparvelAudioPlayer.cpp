//
// Created by sidukov on 05.11.2022.
//

#include "include/SparvelAudioPlayer.h"

// articles and samples to refer to:
// https://github.com/google/oboe/blob/main/docs/GettingStarted.md
// https://chromium.googlesource.com/external/github.com/google/oboe/+/refs/tags/1.1.1/docs/FullGuide.md
// https://github.com/google/oboe/tree/main/samples/RhythmGame

SparvelAudioPlayer::SparvelAudioPlayer(int32_t defaultSampleRate, int32_t defaultFramesPerBurst) {
    this->_sampleRate = defaultSampleRate;
    this->_framesPerBurst = defaultFramesPerBurst;

    oboe::DefaultStreamValues::SampleRate = defaultSampleRate;
    oboe::DefaultStreamValues::FramesPerBurst = defaultFramesPerBurst;
}

bool SparvelAudioPlayer::init()
{
    std::lock_guard <std::mutex> lock(_lock);
    oboe::AudioStreamBuilder builder;

    oboe::Result result = builder.setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::PowerSaving)
            // add a method for figuring out channel count (it might be that audio has only one - quite rare case though)
            ->setChannelCount(oboe::ChannelCount::Stereo)
            ->setSampleRate(_sampleRate)
            ->setSampleRateConversionQuality(oboe::SampleRateConversionQuality::Best)
            ->setFormat(oboe::AudioFormat::Float)
            ->setDirection(oboe::Direction::Output)
            ->setDataCallback(this)
            ->openStream(_stream);

    if (result != oboe::Result::OK)
    {
        __android_log_print(ANDROID_LOG_DEBUG, "NativePlayer", "Failed to create stream. Error: %s", oboe::convertToText(result));
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
