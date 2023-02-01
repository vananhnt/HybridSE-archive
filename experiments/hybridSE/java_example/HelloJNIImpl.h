#ifndef _HELLO_JNI_IMPL_H
#define _HELLO_JNI_IMPL_H

#include <string>


void DoSayHello(const std::string &name);
int DoIncr(const int);


#endif//_HELLO_JNI_IMPL_H
//export JAVA_INC=/usr/lib/jvm/java-8-openjdk-amd64/include
//g++ -std=c++11 -shared -fPIC -I$JAVA_INC -I$JAVA_INC/linux HelloJNIImpl.cpp -o libhello.so
