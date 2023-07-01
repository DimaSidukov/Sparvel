//
// Created by siduk on 18.06.2023.
//

#include "AudioBuffer.h"
#include "Logger.h"

void AudioBuffer::reset() {
    std::lock_guard<std::mutex> lock(mutex);
    buf.reset();
}

size_t AudioBuffer::getCurrentSize() {
    std::lock_guard<std::mutex> lock(mutex);
    return currentIndex;
}

void AudioBuffer::put(float item) {
    std::lock_guard<std::mutex> lock(mutex);
    buf[currentIndex] = item;
    currentIndex++;
}

float AudioBuffer::get(size_t idx) {
    std::lock_guard<std::mutex> lock(mutex);
    return buf[idx];
}
