//
// Created by sidukov on 07.11.2022.
//

#include "include/AudioFileManager.h"

std::vector<float> getAudioStreamFromFile(const std::string& path) {

    std::ifstream inputAudioFile(path, std::ios::binary);

    inputAudioFile.seekg(0, std::ios_base::end);
    std::size_t size = inputAudioFile.tellg();
    inputAudioFile.seekg(0, std::ios_base::beg);
    std::vector<float> v(size/sizeof(float));
    inputAudioFile.read((char*) &v[0], size);
    inputAudioFile.close();

    return v;
}
