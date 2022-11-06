#include <jni.h>
#include <string>
#include "include/SparvelAudioPlayer.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_sparvel_core_functionality_service_AudioManager_play(
        JNIEnv *env,
        jobject thiz,
        jstring path,
        jint defaultSampleRate,
        jint defaultFramesPerBurst
) {
    auto *player = new SparvelAudioPlayer((int32_t)defaultSampleRate, (int32_t)defaultFramesPerBurst);
    player->play_audio();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_sparvel_core_functionality_service_AudioManager_pause(JNIEnv *env, jobject thiz) {
    // TODO: implement pause()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_sparvel_core_functionality_service_AudioManager_finish(JNIEnv *env, jobject thiz) {
    // TODO: implement finish()
}