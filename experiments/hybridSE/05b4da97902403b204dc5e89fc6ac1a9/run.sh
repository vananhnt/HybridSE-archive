#!/bin/bash

echo "Cleaning environment."
ERR=`rm -r coverage.ec coverage.em coverageTotal.ec statematching.txt tmp/ coverage/ event/ bin/ /tmp/jpf-android 2>&1`

echo "Building project."
ERR=`ant clean build dist | tail -n2`
echo $ERR

#JPF="$HOME/Development/JPF"

JPF_CORE="$JPF/jpf-core"
JPF_ANDROID="$JPF/jpf-android/jpf-android"
JPF_NHANDLER="$JPF/jpf-nhandler"
EMMA="$ANDROID_SDK/tools/lib/emma.jar"
CP=build/examples:"${JPF_ANDROID}/build/main":"${JPF_ANDROID}/build/annotations":"${JPF_ANDROID}/build/peers":"${JPF_NHANDLER}/build/main":"${JPF_NHANDLER}/build/peers":"${JPF_NHANDLER}/build/annotations":"${JPF_CORE}/build/main":"${JPF_CORE}/build/annotations":"${JPF_CORE}/build/peers":"${EMMA}":"${JPF_ANDROID}/build/classes":"${JPF_ANDROID}/lib/android-stub.jar":"$JPF_ANDROID/lib/bson-3.0.4.jar":"$JPF_ANDROID/lib/mongo-java-driver-3.1.0.jar":"$JPF_ANDROID/lib/xpp3_min-1.1.4c.jar":"$JPF_ANDROID/lib/xstream-1.4.8.jar":"lib/commons-io-2.5.jar":"$JPF_ANDROID/lib/jsqlparser.jar"  
TIME_START=$(date +%Y/%m/%d\ %H:%M:%S)
echo "CP: ${CP}"

echo "Started: ${TIME_START}."

java -Xmx256G -Xms128G -Dfile.encoding=UTF-8 -classpath "$CP" "$1"

TIME_END=$(date +%Y/%m/%d\ %H:%M:%S)
echo "Done: ${TIME_END}."

S=$'\n'
#alfred-ping  -msg "$1:${S}Started: ${TIME_START}${S}Done: ${TIME_END}."
