#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "de_haidozo_schanner_MainActivity.h"

using namespace std;
using namespace cv;

JNIEXPORT jstring JNICALL Java_de_haidozo_schanner_MainActivity_AnalyzePicture
(JNIEnv *env, jobject obj, jlong imgAddr) {
	Mat& mGr  = *(Mat*)imgAddr;
	if(mGr.data) {
		return env->NewStringUTF("Img transfered :D");
	}
	return env->NewStringUTF("Noooot transfered");
}