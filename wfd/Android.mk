LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := libmediaextractor.c
LOCAL_MODULE := libmediaextractor
LOCAL_MODULE_TAGS := optional

include $(BUILD_SHARED_LIBRARY)
