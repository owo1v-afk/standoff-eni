#include <jni.h>
#include <android/log.h>
#include <cmath>
#include <vector>

#define LOG_TAG "ENI_NATIVE"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_cheat_utils_NativeHook_attach(JNIEnv *env, jobject, jstring pkg) {
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_com_cheat_utils_NativeHook_simulateTap(JNIEnv *env, jobject, jint x, jint y) {
    LOGI("simulateTap %d %d", x, y);
    return JNI_TRUE;
}

JNIEXPORT void JNICALL
Java_com_cheat_utils_NativeHook_injectView(JNIEnv *env, jobject) {
    LOGI("injectView called");
}

JNIEXPORT void JNICALL
Java_com_cheat_utils_NativeHook_setFovRadius(JNIEnv *env, jobject, jfloat radius) {
    LOGI("FOV radius set: %f", radius);
}

}
