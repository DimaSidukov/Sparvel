//
// Created by siduk on 14.05.2023.
//

#pragma once

#include <cstdint>

constexpr int64_t kMillisecondsInSecond = 1000;

constexpr int64_t convertFramesToMillis(const int64_t frames, const int sampleRate) {
    return static_cast<int64_t>((static_cast<double>(frames)/ sampleRate) * kMillisecondsInSecond);
}

constexpr int64_t convertMillisToFrames(const int64_t millis, const int sampleRate) {
    return (millis * sampleRate) / kMillisecondsInSecond;
}
