#include <jni.h>
#include <cstdio>
#include <memory>
#include <android/file_descriptor_jni.h>
#include "DecodingUtilities.h"
#include <android/log.h>
//
// Created by siduk on 16.04.2023.
//

#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, "Audio Engine", __VA_ARGS__))

extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_audiomanager_AudioManager_nativePlay(
    JNIEnv *env,
    jobject thiz,
    jstring filePath,
    jint default_sample_rate,
    jint default_frames_per_burst
) {

    // decoding
    const char* path = env->GetStringUTFChars(filePath, nullptr);
    if (!path) {
        // show error or something
        return;
    }

    // make actual decoding here
    auto test = decodeAudioFile(path);
    // remove string from the memory afterwards
    if (test == nullptr) {
        // callback to show toast
        LOGD("NULL RECEIVED!");
    }
    env->ReleaseStringUTFChars(filePath, path);
//    for(int i = 0; i < 10000; i++) {
//        LOGD("Index at %d is %lf", i, test[i]);
//    }

    // these needed for Oboe
    int defaultSampleRate = static_cast<int>(default_sample_rate);
    int defaultFramesPerBurst = static_cast<int>(default_frames_per_burst);

}