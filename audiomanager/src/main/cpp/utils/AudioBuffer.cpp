//
// Created by siduk on 18.06.2023.
//

#include "AudioBuffer.h"

// https://embeddedartistry.com/blog/2017/05/17/creating-a-circular-buffer-in-c-and-c/
void AudioBuffer::reset() {
    std::lock_guard<std::mutex> lock(mutex);
    head = tail;
    isFull = false;
}

bool AudioBuffer::empty() const {
    return (!isFull && (head == tail));
}

bool AudioBuffer::full() const {
    return isFull;
}

size_t AudioBuffer::capacity() const {
    return maxSize;
}

size_t AudioBuffer::size() const {
    size_t localSize = maxSize;
    if (!isFull) {
        if (head >= tail) {
            localSize = head - tail;
        } else {
            localSize = maxSize + head - tail;
        }
    }
    return localSize;
}

void AudioBuffer::put(float item) {
    std::lock_guard<std::mutex> lock(mutex);

    buf[head] = item;

    if (isFull) {
        if (tail++ == maxSize) {
            tail = 0;
        }
    }
    if (head++ == maxSize) {
        head = 0;
    }

    isFull = head == tail;

}

float AudioBuffer::get() {
    std::lock_guard<std::mutex> lock(mutex);
    if (empty()) {
        return NO_DATA;
    } else {
        auto value = buf[tail];
        if (!isFull) isFull = false;
        if (tail++ == maxSize) {
            tail = 0;
        }
        return value;
    }
}
