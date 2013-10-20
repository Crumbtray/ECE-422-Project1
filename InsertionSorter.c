#include <stdio.h>
#include <jni.h>
#include <stdlib.h>
#include "InsertionSorter.h"

JNIEXPORT jint JNICALL Java_InsertionSorter_binarySort(JNIEnv *env, jobject thisObj, jintArray buf, jdouble failRate)
{
	jsize len;	
	jint *myCopy;
	jboolean *is_copy;
	jintArray result;
	int accessCounter = 0;

	// Store the array length of buf into len
	len = (*env)->GetArrayLength(env, buf);
	myCopy = (jint *) (*env)->GetIntArrayElements(env, buf, is_copy);
	
	// Insertion sort!
	int c, d, t;
	for(c = 1; c < len; c++)
	{
		int d;
		d = c;
		while (d > 0 && myCopy[d] < myCopy[d - 1]) {
			accessCounter = accessCounter + 2;			
			t = myCopy[d];
			accessCounter++;
			myCopy[d] = myCopy[d - 1];
			accessCounter++;
			accessCounter++;			
			myCopy[d - 1] = t;
			accessCounter++;
			d--;
		}
	}

	// Error conditions
	// Perform our failure condition here.

	double hazard = (double) accessCounter * failRate;

	// Seed our random number generator so it's different every time.
	srand(time(NULL));
	double randomNumber = (double)rand() / (double) RAND_MAX;

	double upperBound = 0.5 + hazard;

	if(randomNumber >= 0.5 && randomNumber <= upperBound)
	{
		// Get a failure!
		printf("Failure of secondary.\n");
		return 0;
	}
	(*env)->ReleaseIntArrayElements(env, buf, myCopy, 0);
	return 1;
}
