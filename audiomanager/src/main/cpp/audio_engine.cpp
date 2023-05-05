#include <jni.h>
#include <cstdio>
#include <memory>
#include <android/file_descriptor_jni.h>
//
// Created by siduk on 16.04.2023.
//


//extern "C" {
//    #include <libavutil/timestamp.h>
//    #include <libavformat/avformat.h>
//    #include <libavutil/display.h>
//}

extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_audiomanager_AudioManager_nativePlay(JNIEnv *env, jobject thiz,
                                                      jint file_descriptor,
                                                      jint default_sample_rate,
                                                      jint default_frames_per_burst) {
    int fd = static_cast<int>(file_descriptor);
    int defaultSampleRate = static_cast<int>(defaultSampleRate);
    int defaultFramesPerBurst = static_cast<int>(default_frames_per_burst);

    // there will be call to a decoder and then building the AudioPlayer class
    // decode via ffmpeg

}