//
// Created by siduk on 08.05.2023.
//

#pragma once

extern "C" {
    #include <libavutil/error.h>
    #include <libavcodec/avcodec.h>
}
#include <cstdio>
#include <string>
#include "ffmpeg_headers_wrapper.h"
#include <android/log.h>

void printError(const char* prefix, int errorCode) {
    if (errorCode != 0) {
        const size_t bufsize = 64;
        char buf[bufsize];

        if(av_strerror(errorCode, buf, bufsize) != 0) {
            strcpy(buf, "UNKNOWN_ERROR");
        }
        fprintf(stderr, "%s (%d: %s)\n", prefix, errorCode, buf);
    }
}

int receiveAndHandle(AVCodecContext* codecCtx, AVFrame* frame, float* out);
void handleFrame(const AVCodecContext* codecCtx, const AVFrame* frame, float* out);
int findAudioStream(const AVFormatContext* formatCtx);
void printStreamInformation(const AVCodec* codec, const AVCodecContext* codecCtx, int audioStreamIndex);
float getSample(const AVCodecContext* codecCtx, uint8_t* buffer, int sampleIndex);
void drainDecoder(AVCodecContext* codecCtx, AVFrame* frame, float* out);


#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, "Audio Engine", __VA_ARGS__))

float* decodeAudioFile(const char* path) {
    FILE* inFile = fopen(path, "r");
    if (!inFile) {
        return nullptr;
    }

    AVFormatContext* formatContext = nullptr;
    int err = 0;
    if ((err = avformat_open_input(&formatContext, path, nullptr, nullptr)) != 0) {
        printError("Error opening file: ", err);
        avformat_close_input(&formatContext);
        return nullptr;
    }
    if (!formatContext) {
        avformat_find_stream_info(formatContext, nullptr);
        avformat_close_input(&formatContext);
    }

    // Try to find an audio stream.
    int audioStreamIndex = findAudioStream(formatContext);
    if(audioStreamIndex == -1) {
        // No audio stream was found.
        fprintf(stderr, "None of the available %d streams are audio streams.\n", formatContext->nb_streams);
        avformat_close_input(&formatContext);
        return nullptr;
    }

    // Find the correct decoder for the codec.
    const AVCodec* codec = avcodec_find_decoder(formatContext->streams[audioStreamIndex]->codecpar->codec_id);
    if (codec == nullptr) {
        // Decoder not found.
        fprintf(stderr, "Decoder not found. The codec is not supported.\n");
        avformat_close_input(&formatContext);
        return nullptr;
    }

    // Initialize codec context for the decoder.
    AVCodecContext* codecContext = avcodec_alloc_context3(codec);
    if (codecContext == nullptr) {
        // Something went wrong. Cleaning up...
        avformat_close_input(&formatContext);
        fprintf(stderr, "Could not allocate a decoding context.\n");
        return nullptr;
    }

    // Fill the codecContext with the parameters of the codec used in the read file.
    if ((err = avcodec_parameters_to_context(codecContext, formatContext->streams[audioStreamIndex]->codecpar)) != 0) {
        // Something went wrong. Cleaning up...
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        printError("Error setting codec context parameters.", err);
        return nullptr;
    }

    codecContext->request_sample_fmt = av_get_alt_sample_fmt(codecContext->sample_fmt, 0);

    // Initialize the decoder.
    if ((err = avcodec_open2(codecContext, codec, NULL)) != 0) {
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        return nullptr;
    }

    // Print some interesting file information.
    printStreamInformation(codec, codecContext, audioStreamIndex);

    AVFrame* frame = nullptr;
    if ((frame = av_frame_alloc()) == nullptr) {
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        return nullptr;
    }

    // Prepare the packet.
    AVPacket* packet = av_packet_alloc();

    while ((err = av_read_frame(formatContext, packet)) != AVERROR_EOF) {
        if(err != 0) {
            // Something went wrong.
            printError("Read error.", err);

            // drainDecoder(codecContext, frame, nullptr);
            av_frame_free(&frame);
            avcodec_close(codecContext);
            avcodec_free_context(&codecContext);
            avformat_close_input(&formatContext);
            fclose(inFile);
            return nullptr;
        }
        // ...
    }

    // Does the packet belong to the correct stream?
    if(packet->stream_index != audioStreamIndex) {
        // Free the buffers used by the frame and reset all fields.
        av_packet_unref(packet);
    }

    // We have a valid packet => send it to the decoder.
    if ((err = avcodec_send_packet(codecContext, packet)) == 0) {
        // The packet was sent successfully. We don't need it anymore.
        // => Free the buffers used by the frame and reset all fields.
        av_packet_unref(packet);
    } else {
        // Something went wrong.
        // EAGAIN is technically no error here but if it occurs we would need to buffer
        // the packet and send it again after receiving more frames. Thus we handle it as an error here.
        printError("Send error.", err);

        // drainDecoder(codecContext, frame, outArray);
        av_frame_free(&frame);
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        fclose(inFile);
        return nullptr;
    }

    float* outArray = new float[frame->nb_samples * codecContext->ch_layout.nb_channels];

    // Receive and handle frames.
    // EAGAIN means we need to send before receiving again. So thats not an error.
    if((err = receiveAndHandle(codecContext, frame, outArray)) != AVERROR(EAGAIN)) {
        // Not EAGAIN => Something went wrong.
        printError("Receive error.", err);

        drainDecoder(codecContext, frame, outArray);
        av_frame_free(&frame);
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        fclose(inFile);
        return nullptr;
    }

    // Drain the decoder.
    drainDecoder(codecContext, frame, outArray);

    // Free all data used by the frame.
    av_frame_free(&frame);

    // Close the context and free all data associated to it, but not the context itself.
    avcodec_close(codecContext);

    // Free the context itself.
    avcodec_free_context(&codecContext);

    // We are done here. Close the input.
    avformat_close_input(&formatContext);

    // Close the infile.
    fclose(inFile);

    return outArray;

}

/**
 * Find the first audio stream and returns its index. If there is no audio stream returns -1.
 */
int findAudioStream(const AVFormatContext* formatCtx) {
    int audioStreamIndex = -1;
    for(size_t i = 0; i < formatCtx->nb_streams; ++i) {
        // Use the first audio stream we can find.
        // NOTE: There may be more than one, depending on the file.
        if(formatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioStreamIndex = i;
            break;
        }
    }
    return audioStreamIndex;
}

/*
 * Print information about the input file and the used codec.
 */
void printStreamInformation(const AVCodec* codec, const AVCodecContext* codecCtx, int audioStreamIndex) {
    fprintf(stderr, "Codec: %s\n", codec->long_name);
    if(codec->sample_fmts != nullptr) {
        fprintf(stderr, "Supported sample formats: ");
        for(int i = 0; codec->sample_fmts[i] != -1; ++i) {
            fprintf(stderr, "%s", av_get_sample_fmt_name(codec->sample_fmts[i]));
            if(codec->sample_fmts[i+1] != -1) {
                fprintf(stderr, ", ");
            }
        }
        fprintf(stderr, "\n");
    }
    fprintf(stderr, "---------\n");
    fprintf(stderr, "Stream:        %7d\n", audioStreamIndex);
    fprintf(stderr, "Sample Format: %7s\n", av_get_sample_fmt_name(codecCtx->sample_fmt));
    fprintf(stderr, "Sample Rate:   %7d\n", codecCtx->sample_rate);
    fprintf(stderr, "Sample Size:   %7d\n", av_get_bytes_per_sample(codecCtx->sample_fmt));
    fprintf(stderr, "Channels:      %7d\n", codecCtx->ch_layout.nb_channels);
    fprintf(stderr, "Float Output:  %7s\n", av_sample_fmt_is_planar(codecCtx->sample_fmt) ? "yes" : "no");
}

/**
 * Receive as many frames as available and handle them.
 */
int receiveAndHandle(AVCodecContext* codecCtx, AVFrame* frame, float* out) {
    int err = 0;
    // Read the packets from the decoder.
    // NOTE: Each packet may generate more than one frame, depending on the codec.

    // ERROR HERE!
    while((err = avcodec_receive_frame(codecCtx, frame)) == 0) {
        // Let's handle the frame in a function.
        handleFrame(codecCtx, frame, out);
        // Free any buffers and reset the fields to default values.
        av_frame_unref(frame);
    }
    return err;
}

/**
 * Write the frame to an output file.
 */
void handleFrame(const AVCodecContext* codecCtx, const AVFrame* frame, float* out) {
    LOGD("FRAME SIZE NOW: %d", frame->nb_samples);
    if(av_sample_fmt_is_planar(codecCtx->sample_fmt) == 1) {
        // This means that the data of each channel is in its own buffer.
        // => frame->extended_data[i] contains data for the i-th channel.
        for(int s = 0; s < frame->nb_samples; ++s) {
            for(int c = 0; c < codecCtx->ch_layout.nb_channels; ++c) {
                float sample = getSample(codecCtx, frame->extended_data[c], s);
                //out[s * codecCtx->ch_layout.nb_channels + c] = sample;
            }
        }
    } else {
        for (int s = 0; s < frame->nb_samples; ++s) {
            for (int c = 0; c < codecCtx->ch_layout.nb_channels; ++c) {
                float sample = getSample(codecCtx, frame->extended_data[0],
                                         s * codecCtx->ch_layout.nb_channels + c);
                //out[s * codecCtx->ch_layout.nb_channels + c] = sample;
            }
        }
    }
}

float getSample(const AVCodecContext* codecCtx, uint8_t* buffer, int sampleIndex) {
    int64_t val = 0;
    float ret = 0;
    int sampleSize = av_get_bytes_per_sample(codecCtx->sample_fmt);

    switch(sampleSize) {
        case 1:
            // 8bit samples are always unsigned
            val = reinterpret_cast<uint8_t*>(buffer)[sampleIndex];
            // make signed
            val -= 127;
            break;

        case 2:
            val = reinterpret_cast<int16_t*>(buffer)[sampleIndex];
            break;

        case 4:
            val = reinterpret_cast<int32_t*>(buffer)[sampleIndex];
            break;

        case 8:
            val = reinterpret_cast<int64_t*>(buffer)[sampleIndex];
            break;

        default:
            fprintf(stderr, "Invalid sample size %d.\n", sampleSize);
            return 0;
    }

    // Check which data type is in the sample.
    switch(codecCtx->sample_fmt) {
        case AV_SAMPLE_FMT_U8:
        case AV_SAMPLE_FMT_S16:
        case AV_SAMPLE_FMT_S32:
        case AV_SAMPLE_FMT_U8P:
        case AV_SAMPLE_FMT_S16P:
        case AV_SAMPLE_FMT_S32P:
            // integer => Scale to [-1, 1] and convert to float.
            ret = val / static_cast<float>((1 << (sampleSize* 8 - 1)) - 1);
            break;
        case AV_SAMPLE_FMT_FLT:
        case AV_SAMPLE_FMT_FLTP:
            // float => reinterpret
            ret = *reinterpret_cast<float*>(&val);
            break;
        case AV_SAMPLE_FMT_DBL:
        case AV_SAMPLE_FMT_DBLP:
            // double => reinterpret and then static cast down
            ret = static_cast<float>(*reinterpret_cast<double*>(&val));
            break;
        default:
            fprintf(stderr, "Invalid sample format %s.\n", av_get_sample_fmt_name(codecCtx->sample_fmt));
            return 0;
    }

    return ret;
}

void drainDecoder(AVCodecContext* codecCtx, AVFrame* frame, float* out) {
    int err = 0;
    // Some codecs may buffer frames. Sending NULL activates drain-mode.
    if((err = avcodec_send_packet(codecCtx, nullptr)) == 0) {
        // Read the remaining packets from the decoder.
        err = receiveAndHandle(codecCtx, frame, out);
        if(err != AVERROR(EAGAIN) && err != AVERROR_EOF) {
            // Neither EAGAIN nor EOF => Something went wrong.
            printError("Receive error.", err);
        }
    } else {
        // Something went wrong.
        printError("Send error.", err);
    }
}
