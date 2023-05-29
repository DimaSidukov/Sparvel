//
// Created by siduk on 13.05.2023.
//

#include "AudioPlayer.h"
#include "Arithmetics.h"

oboe::DataCallbackResult
AudioPlayer::onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) {

    auto *outputBuffer = reinterpret_cast<float *>(audioData);

    for (int i = 0; i < numFrames; i++) {
        for (int j = 0; j < channelCount; j++) {
            outputBuffer[i * channelCount + j] = data[currentFrame * channelCount + j];
        }
        if (currentFrame % 1000 == 0) {
            int64_t position = convertFramesToMillis(currentFrame, sampleRate);
            onPositionUpdatedCallback(position);
        }
        if (++currentFrame >= size) {
            currentFrame = 0;
            return oboe::DataCallbackResult::Stop;
        }
    }
    return oboe::DataCallbackResult::Continue;
}

void AudioPlayer::onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) {
    // report to crashlytics or something
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
                    // oboe performs conversion itself but it might consume a lot of cpu for this task
                    // if it really matters, makes sense do resampling when decoding the audio data
            ->setSampleRateConversionQuality(SampleRateConversionQuality::High)
            ->setChannelCount(channelCount)
            ->setCallback(this)
            ->openStream(audioStream);
    if (result != Result::OK) {
        LOGE("Failed to open stream. Error: %s", convertToText(result));
        return;
    }
    currentFrame = 0;
    play();
}

void AudioPlayer::play() {
    oboe::StreamState state = audioStream->getState();
    if (state == oboe::StreamState::Paused
        || state == oboe::StreamState::Open
        || state == oboe::StreamState::Stopped
            ) {
        audioStream->requestFlush();
    }
    oboe::Result result = audioStream->requestStart();
    if (result != oboe::Result::OK) {
        // inform crashlytics?
        return;
    }
}

void AudioPlayer::pause() {
    if (audioStream->getState() == oboe::StreamState::Started) {
        audioStream->requestPause();
    } else audioStream->requestStart();
}

void AudioPlayer::stop() {
    audioStream->requestStop();
    onPositionUpdatedCallback(0);
    currentFrame = 0;
}

void AudioPlayer::seek(int64_t position) {
    currentFrame = convertMillisToFrames(position, sampleRate);
}
