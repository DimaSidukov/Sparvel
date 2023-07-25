//
// Created by siduk on 13.05.2023.
//

#include "FFmpegDecoder.h"

void FFmpegDecoder::printError(const char *prefix, int errorCode) {
#ifdef NDEBUG
#else
    if (errorCode != 0) {
        const size_t bufsize = 64;
        char buf[bufsize];

        if (av_strerror(errorCode, buf, bufsize) != 0) {
            strcpy(buf, "UNKNOWN_ERROR");
        }
        LOGD("%s (%d: %s)\n", prefix, errorCode, buf);
    }
#endif
}

/**
 * Find the first audio stream and returns its index. If there is no audio stream returns -1.
 */
int FFmpegDecoder::findAudioStream(const AVFormatContext *formatCtx) {
    int audioStreamIndex = -1;
    for (size_t i = 0; i < formatCtx->nb_streams; ++i) {
        // Use the first audio stream we can find.
        // NOTE: There may be more than one, depending on the file.
        if (formatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioStreamIndex = i;
            break;
        }
    }
    return audioStreamIndex;
}

/*
 * Print information about the input file and the used codec.
 */
void FFmpegDecoder::printStreamInformation(
        const AVCodec *codec,
        const AVCodecContext *codecCtx,
        int audioStreamIndex
) {
    LOGD("Codec: %s\n", codec->long_name);
    if (codec->sample_fmts != nullptr) {
        LOGD("Supported sample formats: ");
        for (int i = 0; codec->sample_fmts[i] != -1; ++i) {
            LOGD("%s", av_get_sample_fmt_name(codec->sample_fmts[i]));
            if (codec->sample_fmts[i + 1] != -1) {
                LOGD(", ");
            }
        }
        LOGD("\n");
    }
    LOGD("---------\n");
    LOGD("Stream:        %7d\n", audioStreamIndex);
    LOGD("Sample Format: %7s\n", av_get_sample_fmt_name(codecCtx->sample_fmt));
    LOGD("Sample Rate:   %7d\n", codecCtx->sample_rate);
    LOGD("Sample Size:   %7d\n", av_get_bytes_per_sample(codecCtx->sample_fmt));
    LOGD("Channels:      %7d\n", codecCtx->ch_layout.nb_channels);
    LOGD("Float Output:  %7s\n", av_sample_fmt_is_planar(codecCtx->sample_fmt) ? "yes" : "no");
}

/**
 * Receive as many frames as available and handle them.
 */
int FFmpegDecoder::receiveAndHandle(AudioBuffer *output) {
    int err = 0;
    // Read the packets from the decoder.
    // NOTE: Each packet may generate more than one frame, depending on the codec.

    while ((err = avcodec_receive_frame(codecContext, frame)) == 0) {
        // Let's handle the frame in a function.
        handleFrame(output);
        // Free any buffers and reset the fields to default values.
        av_frame_unref(frame);
    }
    return err;
}

/**
 * Write the frame to an output file.
 */
void FFmpegDecoder::handleFrame(AudioBuffer *output) {
    if (av_sample_fmt_is_planar(codecContext->sample_fmt) == 1) {
        // This means that the data of each channel is in its own buffer.
        // => frame->extended_data[i] contains data for the i-th channel.
        for (int s = 0; s < frame->nb_samples; ++s) {
            for (int c = 0; c < codecContext->ch_layout.nb_channels; ++c) {
                float sample = getSample(codecContext, frame->extended_data[c], s);
                output->put(sample);
            }
        }
    } else {
        for (int s = 0; s < frame->nb_samples; ++s) {
            for (int c = 0; c < codecContext->ch_layout.nb_channels; ++c) {
                float sample = getSample(codecContext, frame->extended_data[0],
                                         s * codecContext->ch_layout.nb_channels + c);
                output->put(sample);
            }
        }
    }
}

float FFmpegDecoder::getSample(const AVCodecContext *codecCtx, uint8_t *buffer, int sampleIndex) {
    int64_t val = 0;
    float ret = 0;
    int sampleSize = av_get_bytes_per_sample(codecCtx->sample_fmt);

    switch (sampleSize) {
        case 1:
            // 8bit samples are always unsigned
            val = reinterpret_cast<uint8_t *>(buffer)[sampleIndex];
            // make signed
            val -= 127;
            break;

        case 2:
            val = reinterpret_cast<int16_t *>(buffer)[sampleIndex];
            break;

        case 4:
            val = reinterpret_cast<int32_t *>(buffer)[sampleIndex];
            break;

        case 8:
            val = reinterpret_cast<int64_t *>(buffer)[sampleIndex];
            break;

        default:
            LOGD("Invalid sample size %d.\n", sampleSize);
            return 0;
    }

    // Check which data type is in the sample.
    switch (codecCtx->sample_fmt) {
        case AV_SAMPLE_FMT_U8:
        case AV_SAMPLE_FMT_S16:
        case AV_SAMPLE_FMT_S32:
        case AV_SAMPLE_FMT_U8P:
        case AV_SAMPLE_FMT_S16P:
        case AV_SAMPLE_FMT_S32P:
            // integer => Scale to [-1, 1] and convert to float.
            ret = val / static_cast<float>((1 << (sampleSize * 8 - 1)) - 1);
            break;
        case AV_SAMPLE_FMT_FLT:
        case AV_SAMPLE_FMT_FLTP:
            // float => reinterpret
            ret = *reinterpret_cast<float *>(&val);
            break;
        case AV_SAMPLE_FMT_DBL:
        case AV_SAMPLE_FMT_DBLP:
            // double => reinterpret and then static cast down
            ret = static_cast<float>(*reinterpret_cast<double *>(&val));
            break;
        default:
            fprintf(stderr, "Invalid sample format %s.\n",
                    av_get_sample_fmt_name(codecCtx->sample_fmt));
            return 0;
    }

    return ret;
}

void FFmpegDecoder::drainDecoder(AudioBuffer *output, int numFrames) {
    int err;
    // Some codecs may buffer frames. Sending NULL activates drain-mode.
    if ((err = avcodec_send_packet(codecContext, nullptr)) == 0) {
        // Read the remaining packets from the decoder.
        err = receiveAndHandle(output);
        if (err != AVERROR(EAGAIN) && err != AVERROR_EOF) {
            // Neither EAGAIN nor EOF => Something went wrong.
            printError("Receive error.", err);
        }
    } else {
        // Something went wrong.
        printError("Send error.", err);
    }
}

FFmpegDecoder::FFmpegDecoder(
        int &sampleRate,
        int &channelCount,
        const char *filePath,
        size_t &arraySize) : path(filePath) {
    inFile = fopen(filePath, "r");
    if (!inFile) {
        free(inFile);
        return;
    }

    formatContext = nullptr;
    int err;
    if ((err = avformat_open_input(&formatContext, path, nullptr, nullptr)) != 0) {
        printError("Error opening file: ", err);
        return;
    }

    avformat_find_stream_info(formatContext, nullptr);

    // Try to find an audio stream.
    audioStreamIndex = findAudioStream(formatContext);
    if (audioStreamIndex == -1) {
        // No audio stream was found.
        LOGD("None of the available %d streams are audio streams.\n", formatContext->nb_streams);
        avformat_close_input(&formatContext);
        return;
    }

    // Find the correct decoder for the codec.
    codec = avcodec_find_decoder(
            formatContext->streams[audioStreamIndex]->codecpar->codec_id);
    if (codec == nullptr) {
        // Decoder not found.
        LOGD("Decoder not found. The codec is not supported.\n");
        avformat_close_input(&formatContext);
        return;
    }

    // Initialize codec context for the decoder.
    codecContext = avcodec_alloc_context3(codec);
    codecContext->thread_count = 4;
    codecContext->thread_type = FF_THREAD_FRAME;
    if (codecContext == nullptr) {
        // Something went wrong. Cleaning up...
        avformat_close_input(&formatContext);
        LOGD("Could not allocate a decoding context.\n");
        return;
    }

    // Fill the codecContext with the parameters of the codec used in the read file.
    if ((err = avcodec_parameters_to_context(codecContext,
                                             formatContext->streams[audioStreamIndex]->codecpar)) !=
        0) {
        // Something went wrong. Cleaning up...
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        LOGD("Error setting codec context parameters. %d", err);
        return;
    }

    codecContext->request_sample_fmt = av_get_alt_sample_fmt(codecContext->sample_fmt, 0);

    // Initialize the decoder.
    if ((err = avcodec_open2(codecContext, codec, nullptr)) != 0) {
        printError("Error initializing a decoder", err);
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        return;
    }
    sampleRate = codecContext->sample_rate;
    channelCount = codecContext->ch_layout.nb_channels;

    // Print some interesting file information.
    printStreamInformation(codec, codecContext, audioStreamIndex);

    frame = nullptr;
    if ((frame = av_frame_alloc()) == nullptr) {
        avcodec_close(codecContext);
        avcodec_free_context(&codecContext);
        avformat_close_input(&formatContext);
        return;
    }

    auto size = static_cast<long long>(
            pow(2, av_get_bytes_per_sample(codecContext->sample_fmt))
            * sampleRate
            * ((double) (formatContext->duration) / AV_TIME_BASE)
            * channelCount
    );

    arraySize = size;
}

void FFmpegDecoder::decodePacket(AudioBuffer *outputPacket) {
    AVPacket *packet = av_packet_alloc();

    int err;
    while ((err = av_read_frame(formatContext, packet)) != AVERROR_EOF && !shouldStopExecution) {
        if (err != 0) {
            printError("Read error.", err);
            break;
        }
        // Does the packet belong to the correct stream?
        if (packet->stream_index != audioStreamIndex) {
            // Free the buffers used by the frame and reset all fields.
            av_packet_unref(packet);
            continue;
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
            break;
        }
        // Receive and handle frames.
        // EAGAIN means we need to send before receiving again. So thats not an error.
        if ((err = receiveAndHandle(outputPacket)) != AVERROR(EAGAIN)) {
            // Not EAGAIN => Something went wrong.
            printError("Receive error.", err);
            break;
        }
    }
    outputPacket->setFillCompleted();
}

FFmpegDecoder::~FFmpegDecoder() {
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

    delete path;
}

void FFmpegDecoder::stopExecution() {
    shouldStopExecution = true;
}
