//
// Created by siduk on 13.05.2023.
//

#include "AudioPlayer.h"

void AudioPlayer::play() {

}

oboe::DataCallbackResult
AudioPlayer::onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) {

    auto *outputBuffer = reinterpret_cast<float *>(audioData);

    for(int i = 0; i < numFrames; i++) {
        for(int j = 0; j < channelCount; j++) {
            outputBuffer[i * channelCount + j] = data[currentFrame * channelCount + j];
        }
        if (++currentFrame >= size) {
            currentFrame = 0;
            return oboe::DataCallbackResult::Stop;
        }
    }
    return oboe::DataCallbackResult::Continue;
}

void AudioPlayer::onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) {
    AudioStreamErrorCallback::onErrorAfterClose(oboeStream, error);
}

void AudioPlayer::onErrorBeforeClose(oboe::AudioStream *oboeStream, oboe::Result error) {
    AudioStreamErrorCallback::onErrorBeforeClose(oboeStream, error);
}

bool AudioPlayer::onError(oboe::AudioStream *oboeStream, oboe::Result error) {
    return AudioStreamErrorCallback::onError(oboeStream, error);
}

void AudioPlayer::init() {
    using namespace oboe;
    Result result = AudioStreamBuilder().setFormat(AudioFormat::Float)
            ->setDirection(Direction::Output)
            ->setFormatConversionAllowed(true)
            ->setFramesPerCallback(gFramesPerCallback)
            ->setUsage(Usage::Media)
            ->setContentType(ContentType::Music)
            ->setPerformanceMode(PerformanceMode::LowLatency)
            ->setSharingMode(SharingMode::Shared)
            ->setSampleRate(sampleRate)
            // does oboe perform sample rate conversion itself? it means, i won't need to perform
            // conversion myself
            ->setSampleRateConversionQuality(SampleRateConversionQuality::High)
            ->setChannelCount(channelCount)
            ->setCallback(this)
            ->openStream(audioStream);
    if (result != Result::OK) {
        LOGE("Failed to open stream. Error: %s", convertToText(result));
        return;
    }
    currentFrame = 0;

    using oboe::StreamState;
    StreamState state = audioStream->getState();
    if (state == StreamState::Paused || state == StreamState::Open || state == StreamState::Stopped) {
        audioStream->requestFlush();
    }
    audioStream->requestStart();
}
