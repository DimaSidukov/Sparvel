//
// Created by siduk on 18.06.2023.
//

#pragma once

#include <mutex>
#include <memory>

class AudioBuffer {
public:
    explicit AudioBuffer(size_t size): buf(std::unique_ptr<float[]>(new float[size])) {

    }

    void put(float item);
    float get(size_t idx);
    size_t getCurrentSize();
    void reset();

private:
    std::mutex mutex;
    size_t currentIndex { 0 };
    std::unique_ptr<float[]> buf;
};
