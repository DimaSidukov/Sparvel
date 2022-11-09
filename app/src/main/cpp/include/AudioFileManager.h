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

std::vector<float> getAudioStreamFromFile(const std::string& path);

#endif //SPARVEL_AUDIOFILEMANAGER_H
