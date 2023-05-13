#include <jni.h>
#include <cstdio>
#include <memory>
#include <android/file_descriptor_jni.h>
#include "AudioPlayer.h"
#include "DecodingUtilities.h"
//
// Created by siduk on 16.04.2023.
//

static std::unique_ptr<AudioPlayer> audioPlayer;

static JavaVM *gVm = nullptr;
static jobject gThis = nullptr;
static jclass gCls = nullptr;
static jmethodID gMtd = nullptr;

void showToast(const char *message) {
    if (!gThis) return;
    JNIEnv *jniEnv = nullptr;
    if (gVm->GetEnv((void **) &jniEnv, JNI_VERSION_1_6) != JNI_OK) return;
    if (!gCls) gCls = jniEnv->GetObjectClass(gThis);
    if (!gMtd) gMtd = jniEnv->GetMethodID(gCls, "showToast", "(Ljava/lang/String;)V");
    // if another language selected (russian or french) it makes sense sending a key name
    // and extract it from strings.xml
    jniEnv->CallVoidMethod(gThis, gMtd, jniEnv->NewStringUTF(message));
    free(jniEnv);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *asReserved) {
    gVm = vm;
    return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_audiomanager_AudioManager_nativePlay(
        JNIEnv *env,
        jobject thiz,
        jstring filePath,
        jint default_sample_rate,
        jint default_frames_per_burst
) {

    if (!gThis) {
        env->GetJavaVM(&gVm);
        gThis = env->NewGlobalRef(thiz);
    }

    // sample rate should be passed to decoder to resample the audio if its original
    // sample rate is bigger than the one that the audio device can produce
    int defaultSampleRate = static_cast<int>(default_sample_rate);

    // decoding
    const char *path = env->GetStringUTFChars(filePath, nullptr);
    if (!path) {
        showToast("Error opening the file");
        return;
    }

    DecodedData* test = decodeAudioFile(path);
    if (test->data == nullptr || test->size == 0) {
        showToast("An error occurred while reading the file");
        env->ReleaseStringUTFChars(filePath, path);
        return;
    }
    env->ReleaseStringUTFChars(filePath, path);

    gFramesPerCallback = static_cast<int>(default_frames_per_burst);

    audioPlayer = std::make_unique<AudioPlayer>(test);

}