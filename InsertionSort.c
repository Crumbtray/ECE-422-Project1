#include <stdio.h>
#include <jni.h>
#include "InsertionSort.h"

JNIEXPORT void JNICALL Java_InsertionSort_binarySort(JNIEnv *env, jobject thisObj, jintArray buf)
{
	printf("Hello World!\n");
}