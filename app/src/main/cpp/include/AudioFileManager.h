//
// Created by siduk on 07.11.2022.
//

#ifndef SPARVEL_AUDIOFILEMANAGER_H
#define SPARVEL_AUDIOFILEMANAGER_H

#include <stdlib.h>
#include <string>
#include <fstream>
#include <iterator>
#include <vector>
#include <oboe/AudioStream.h>
#include <media/NdkMediaCodec.h>
#include <media/NdkMediaExtractor.h>
#include <oboe/Oboe.h>
#include <android/log.h>

std::vector<float> getAudioStreamFromFile(const std::string& path);

#endif //SPARVEL_AUDIOFILEMANAGER_H
