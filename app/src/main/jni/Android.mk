LOCAL_PATH := $(call my-dir)/src/

include $(CLEAR_VARS)

# OpenCV

OPENCV_ROOT := /Users/Shylock/Workspaces/Android/noopu/OpenCV-2.4.9-android-sdk

# OPENCV_JNI := ${OPENCV_ROOT}/sdk/native/jni

OPENCV_JNI := $(LOCAL_PATH)/../../../../../openCVLibrary249/src/main/jni


OPENCV_CAMERA_MODULES:=on

OPENCV_INSTALL_MODULES:=on

include $(OPENCV_JNI)/OpenCV.mk


LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

LOCAL_C_INCLUDES += $(OPENCV_JNI)/include




FILE_LIST := $(wildcard $(LOCAL_PATH)/core/*.cpp)

LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)


LOCAL_MODULE     := imageproc
LOCAL_LDLIBS += -llog 
include $(BUILD_SHARED_LIBRARY)

