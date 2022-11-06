#include <jni.h>
#include <string>
#include "include/SparvelAudioPlayer.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_sparvel_core_functionality_service_AudioManager_play(JNIEnv *env, jobject thiz, jstring path) {
    auto *player = new SparvelAudioPlayer();
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