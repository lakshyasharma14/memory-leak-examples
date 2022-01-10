#include <jni.h>
#include "Leak3.h"
#include <stdio.h>
#include <iostream>


void garbage()
{
    int* p = (int*) malloc(1000000 * sizeof(int));
    int* q = (int*) malloc(1000000 * sizeof(int));
    int* r = (int*) malloc(1000000 * sizeof(int));
    int* s = (int*) malloc(1000000 * sizeof(int));
    static int *d =(int*)  malloc(1000000 * sizeof(int));
    return;
}

JNIEXPORT void JNICALL Java_Leak3_displayHelloWorld
(JNIEnv* env, jobject thisObject) {
    garbage();
    std::cout << "Hello from C++ !!" << std::endl;
}