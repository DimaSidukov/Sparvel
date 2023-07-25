//
// Created by siduk on 18.06.2023.
//

#pragma once

#include <mutex>
#include <memory>
#include <vector>
#include <thread>

class AudioBuffer {
public:
    explicit AudioBuffer(size_t size): buf(new float[size]) {

    }

    void put(float item);

    float get(size_t idx);

    size_t getCurrentSize();

    void reset();

    bool isFillCompleted() const;

    void setFillCompleted();

    void stopExecution();

private:
    std::thread cleaningThread;
    std::atomic<bool> shouldStopExecution = false;
    std::mutex mutex;
    size_t bufferSize{0};
    float* buf;
    bool _isFillCompleted = false;
};
