#include <jni.h>
#include "Leak3.h"
#include <stdio.h>
#include <iostream>

void garbage()
{
    int* ptr = new int(5000);
    return;
}

JNIEXPORT void JNICALL Java_Leak3_displayHelloWorld
(JNIEnv* env, jobject thisObject) {
    garbage();
    std::cout << "Hello from C++ !!" << std::endl;
}