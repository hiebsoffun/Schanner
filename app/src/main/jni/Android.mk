LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT:= C:\Users\hieblmi\AppData\Local\Android\OpenCV-2.4.10-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

LOCAL_MODULE := sudoku_board_recognition
LOCAL_SRC_FILES := main.cpp
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)