//
// Created by siduk on 08.05.2023.
//

#pragma once

#include <cstdio>
#include <string>
#include "ffmpeg_headers_wrapper.h"
#include "Logger.h"
#include "AudioBuffer.h"
#include "Helpers.h"

class FFmpegDecoder {
private:
    const char *path;
    FILE *inFile;
    AVFormatContext *formatContext;
    AVCodecContext *codecContext;
    const AVCodec *codec;
    AVFrame *frame;
    int audioStreamIndex;
    std::atomic<bool> shouldStopExecution = false;

    void printError(const char *prefix, int errorCode);

    int receiveAndHandle(AudioBuffer *output);

    void handleFrame(AudioBuffer *output);

    int findAudioStream(const AVFormatContext *formatCtx);

    static void printStreamInformation(const AVCodec *codec, const AVCodecContext *codecCtx,
                                       int audioStreamIndex);

    float getSample(const AVCodecContext *codecCtx, uint8_t *buffer, int sampleIndex);

    void drainDecoder(AudioBuffer *output, int numFrames);

public:

    FFmpegDecoder(int &sampleRate, int &channelCount, const char *path, size_t &arraySize);

    ~FFmpegDecoder();

    void decodePacket(AudioBuffer *outputPacket);

    void stopExecution();

};
