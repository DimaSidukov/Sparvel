//
// Created by sidukov on 05.11.2022.
//

#ifndef SPARVEL_SPARVELAUDIOPLAYER_H
#define SPARVEL_SPARVELAUDIOPLAYER_H

#include <oboe/Oboe.h>
#include "AudioPlayer.h"

class SparvelAudioPlayer :
        public AudioPlayer,
        public oboe::AudioStreamDataCallback {
private:
    std::shared_ptr <oboe::AudioStream> _stream;
    std::mutex _lock;
public:
    virtual ~SparvelAudioPlayer() = default;

    void init();

    void play_audio();

    void pause_audio();

    void finish_process();

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames);
};

#endif //SPARVEL_SPARVELAUDIOPLAYER_H
