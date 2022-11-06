//
// Created by sidukov on 05.11.2022.
//

#ifndef SPARVEL_AUDIOPLAYER_H
#define SPARVEL_AUDIOPLAYER_H

class AudioPlayer {
public:
    virtual ~AudioPlayer() = default;
    virtual bool init() = 0;
    virtual void play_audio() = 0;
    virtual void pause_audio() = 0;
    virtual void finish_process() = 0;
};

#endif //SPARVEL_AUDIOPLAYER_H
