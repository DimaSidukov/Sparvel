//
// Created by siduk on 08.05.2023.
//

#pragma once

#include <cstdio>
#include <string>
#include "ffmpeg_headers_wrapper.h"
#include "Logger.h"

struct DecodedData {
    float *data;
    size_t size;
    int sampleRate;
    int channelCount;
};

void printError(const char *prefix, int errorCode);

int receiveAndHandle(AVCodecContext *codecCtx, AVFrame *frame, DecodedData *out);

void handleFrame(const AVCodecContext *codecCtx, const AVFrame *frame, DecodedData *out);

int findAudioStream(const AVFormatContext *formatCtx);

void
printStreamInformation(const AVCodec *codec, const AVCodecContext *codecCtx, int audioStreamIndex);

float getSample(const AVCodecContext *codecCtx, uint8_t *buffer, int sampleIndex);

void drainDecoder(AVCodecContext *codecCtx, AVFrame *frame, DecodedData *out);

DecodedData *decodeAudioFile(const char *path);
