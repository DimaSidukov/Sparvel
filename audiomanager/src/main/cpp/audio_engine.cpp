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

static JavaVM *virtualMachine = nullptr;
static jobject thisObject = nullptr;
static JNIEnv *toastEnv = nullptr;
static JNIEnv *positionEnv = nullptr;

void showToast(const char *message) {
    if (!thisObject) return;
    if (!toastEnv) {
        if (virtualMachine->GetEnv((void **) &toastEnv, JNI_VERSION_1_6) != JNI_OK) return;
    }
    jclass objectClass = toastEnv->GetObjectClass(thisObject);
    jmethodID objectMethod = toastEnv->GetMethodID(objectClass, "showToast",
                                                   "(Ljava/lang/String;)V");
    // if another language selected (russian or french) it makes sense sending a key name
    // and extract it from strings.xml
    toastEnv->CallVoidMethod(thisObject, objectMethod, toastEnv->NewStringUTF(message));
}

void onPositionUpdated(int64_t position) {
    if (!thisObject) return;
    // if (!positionEnv) {
    if (virtualMachine->GetEnv((void **) &positionEnv, JNI_VERSION_1_6) != JNI_OK) return;
    // }
    jclass objectClass = positionEnv->GetObjectClass(thisObject);
    jmethodID objectMethod = positionEnv->GetMethodID(objectClass, "onPositionUpdated", "(J)V");
    positionEnv->CallVoidMethod(thisObject, objectMethod, position);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *asReserved) {
    virtualMachine = vm;
    return JNI_VERSION_1_6;
}

// might crash? https://stackoverflow.com/questions/9644450/jni-cleanup-and-daemon-threads-in-android-ndk
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *asReserved) {
    free(virtualMachine);
    free(thisObject);
    free(toastEnv);
    free(positionEnv);
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

    if (!thisObject) {
        env->GetJavaVM(&virtualMachine);
        thisObject = env->NewGlobalRef(thiz);
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

    gFramesPerCallback = static_cast<int>(default_frames_per_burst);

    if (!audioPlayer) audioPlayer = std::make_unique<AudioPlayer>(path, onPositionUpdated);
    else audioPlayer->update(path);

    env->ReleaseStringUTFChars(filePath, path);

}
extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_audiomanager_AudioManager_nativePause(JNIEnv *env, jobject thiz) {
    if (audioPlayer) audioPlayer->pause();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_audiomanager_AudioManager_nativeFinish(JNIEnv *env, jobject thiz) {
    if (audioPlayer) {
        audioPlayer.reset();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_sidukov_audiomanager_AudioManager_nativeSeek(JNIEnv *env, jobject thiz, jlong position) {
    if (audioPlayer) audioPlayer->seek(static_cast<int64_t>(position));
}