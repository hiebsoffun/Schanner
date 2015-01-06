#include <jni.h>
#include "de_haidozo_schanner_MainActivity.h"

JNIEXPORT jstring JNICALL Java_de_haidozo_schanner_MainActivity_hello
(JNIEnv *env, jobject obj) {
	return env->NewStringUTF("Hello from JNI");
}