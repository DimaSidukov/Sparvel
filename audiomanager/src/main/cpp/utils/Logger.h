//
// Created by siduk on 13.05.2023.
//

#pragma once

#include <android/log.h>

#ifdef NDEBUG
#define LOGD(...)
#define LOGE(...)
#else
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, "Audio Engine", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "Audio Engine", __VA_ARGS__))
#endif
