//
// Created by siduk on 18.06.2023.
//

#include "AudioBuffer.h"
#include "Logger.h"

void AudioBuffer::reset() {
    std::lock_guard<std::mutex> lock(mutex);
    cleaningThread.join();
    delete[] buf;
}

size_t AudioBuffer::getCurrentSize() {
    std::lock_guard<std::mutex> lock(mutex);
    return bufferSize;
}

void AudioBuffer::put(float item) {
    std::lock_guard<std::mutex> lock(mutex);
    buf[bufferSize] = item;
    bufferSize++;
}

float AudioBuffer::get(size_t idx) {
    std::lock_guard<std::mutex> lock(mutex);
    return buf[idx];
}

bool AudioBuffer::isFillCompleted() const {
    return _isFillCompleted;
}

void AudioBuffer::setFillCompleted() {
    cleaningThread = std::thread([&] {
        float *new_arr = new float[bufferSize];
        for (size_t i = 0; i < bufferSize; i++) {
            if (shouldStopExecution) break;
            new_arr[i] = buf[i];
        }
        std::lock_guard<std::mutex> lock(mutex);
        delete[] buf;
        _isFillCompleted = true;
        if (!shouldStopExecution) buf = new_arr;
        else delete[] new_arr;
    });
}

void AudioBuffer::stopExecution() {
    shouldStopExecution = true;
    cleaningThread.join();
}
