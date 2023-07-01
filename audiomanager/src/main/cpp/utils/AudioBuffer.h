//
// Created by siduk on 18.06.2023.
//

#pragma once

#include <mutex>
#include <memory>

class AudioBuffer {
public:
    explicit AudioBuffer(size_t size):
        buf(std::unique_ptr<float[]>(new float[size])),
        maxSize(size)
    { }

    void put(float item);
    float get();
    void reset();
    bool empty() const;
    bool full() const;
    size_t capacity() const;
    size_t size() const;

    constexpr static const float NO_DATA = -10;

private:
    std::mutex mutex;
    std::unique_ptr<float[]> buf;
    size_t head = 0;
    size_t tail = 0;
    const size_t maxSize;
    bool isFull = false;
};
