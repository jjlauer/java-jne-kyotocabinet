#!/bin/bash

echo "This script is not complete..."
exit

LZO_ENABLED=0

MACHINE_TYPE=`uname -m`
if [ ${MACHINE_TYPE} == 'x86_64' ]; then
  MYARCH="x64"
else
  MYARCH="x32"
fi

echo "Building for $MYARCH..."

BUILD_ROOT="native/linux/$MYARCH"

mkdir -p ${BUILD_ROOT}/sysroot
cd ${BUILD_ROOT}

if [ $LZO_ENABLED != 0 ]; then
  echo "LZO enabled; building library..."
fi
