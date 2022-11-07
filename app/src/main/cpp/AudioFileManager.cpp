//
// Created by sidukov on 07.11.2022.
//

#include <android/log.h>
#include "include/AudioFileManager.h"
#include <oboe/AudioStream.h>

char* getAudioStreamFromFile(const std::string& path) {

    std::ifstream inputAudioFile(path, std::ios::binary);

    std::vector<char> audioAsBytes(
            (std::istreambuf_iterator<char>(inputAudioFile)),
            (std::istreambuf_iterator<char>()));

    inputAudioFile.close();

    return reinterpret_cast<char*>(audioAsBytes.data());
}
