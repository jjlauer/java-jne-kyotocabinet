
## Prerequisites

Install Oracle JDK 7 (build 51 as of this doc)
  http://java.oracle.com/

Make sure JAVA_HOME is set to your JDK!!
  export JAVA_HOME=$(/usr/libexec/java_home)


## Set the os and arch you are targeting a build for
export BUILD_OS=osx
export VER_LZO="2.06"
export VER_LZMA="5.0.5"
export VER_KC="1.2.76"
export VER_KC_JAVA="1.24"
MACHINE_TYPE=`uname -m`
if [ ${MACHINE_TYPE} == 'x86_64' ]; then
  BUILD_ARCH="x64"
else
  BUILD_ARCH="x32"
fi


export BUILD_ROOT=native/${BUILD_OS}-${BUILD_ARCH}
mkdir -p ${BUILD_ROOT}/sysroot
cd ${BUILD_ROOT}


# no realpath on osx...
cat > ./realpath <<EOF
#!/bin/sh
realpath() {
  OURPWD=$PWD
  cd "\$(dirname "\$1")"
  LINK=\$(readlink "\$(basename "\$1")")
  while [ "\$LINK" ]; do
    cd "\$(dirname "\$LINK")"
    LINK=\$(readlink "\$(basename "\$1")")
  done
  REALPATH="\$PWD/\$(basename "\$1")"
  cd "\$OURPWD"
  echo "\$REALPATH"
}
realpath "\$@"
EOF

chmod +x ./realpath

export PATH=$PATH:$PWD


# Z lib (usually already present on linux systems; skip building it; make sure to have the headers though)
# wget http://zlib.net/zlib-1.2.8.tar.gz



# LZO lib
wget http://www.oberhumer.com/opensource/lzo/download/lzo-${VER_LZO}.tar.gz
tar zxvf lzo-${VER_LZO}.tar.gz
cd lzo-${VER_LZO}

CFLAGS="-mmacosx-version-min=10.5" \
LDFLAGS="-mmacosx-version-min=10.5" ABI=32 \
./configure --prefix=$(realpath ../sysroot) --enable-static --disable-shared --with-pic 

make
make install
cd ..


# LZMA lib
wget http://tukaani.org/xz/xz-${VER_LZMA}.tar.gz
tar zxvf xz-${VER_LZMA}.tar.gz
cd xz-${VER_LZMA}

CFLAGS="-mmacosx-version-min=10.5" \
LDFLAGS="-mmacosx-version-min=10.5" ABI=32 \
./configure --prefix=$(realpath ../sysroot) --disable-xz --disable-xzdec --disable-lzmadec --disable-lzmainfo --disable-shared --with-pic

make
make install
cd ..


wget http://fallabs.com/kyotocabinet/pkg/kyotocabinet-${VER_KC}.tar.gz
tar zxvf kyotocabinet-${VER_KC}.tar.gz
cd kyotocabinet-${VER_KC}

# statically compile in all dependencies
CPPFLAGS="-mmacosx-version-min=10.5 -I$(realpath ../sysroot/include)" \
LDFLAGS="-mmacosx-version-min=10.5 -L$(realpath ../sysroot/lib)" ABI=32 \
./configure --prefix=$(realpath ../sysroot) --enable-lzo --enable-lzma


make
make install


cd ..


# delete all dylibs to force linking to static lib
rm -Rf sysroot/lib/*.dylib


# java wrapper to lib
wget http://fallabs.com/kyotocabinet/javapkg/kyotocabinet-java-${VER_KC_JAVA}.tar.gz
tar zxvf kyotocabinet-java-${VER_KC_JAVA}.tar.gz
cd kyotocabinet-java-${VER_KC_JAVA}


LIBS="-llzo2 -llzma" \
CXXFLAGS="-mmacosx-version-min=10.5 -I$JAVA_HOME/include -I$JAVA_HOME/include/darwin" \
LDFLAGS="-mmacosx-version-min=10.5" ABI=32 \
./configure --with-kc=$(realpath ../sysroot) --prefix=$(realpath ../sysroot)

# kyoto's configure script is broken -- it uses the environment vars to finish
# but then doesn't include them in the resulting makefile
# fix "<java_home>/include/mac" to "<java_home>/include/darwin"

sed -i -e "s/Contents\/Home\/include\/mac/Contents\/Home\/include\/darwin/" Makefile
sed -i -e "s/CXXFLAGS = /CXXFLAGS = -mmacosx-version-min=10.5 /" Makefile

make
make install


# correct libjkyotocabinet.so now installed in ../sysroot/lib

cd ../../..
mkdir -p jne/${BUILD_OS}/${BUILD_ARCH}
cp ${BUILD_ROOT}/sysroot/lib/libjkyotocabinet.so jne/${BUILD_OS}/${BUILD_ARCH}/


# ONLY IF YOU ARE PLANNING ON UPDATING UNDERLYING JAVA CODE...
# copy everything but Loader.java and Test.java to main kyotocabinet...
rm ${BUILD_ROOT}/kyotocabinet-java-1.24/Loader.java
mv ${BUILD_ROOT}/kyotocabinet-java-1.24/Test.java src/test/java/kyotocabinet/
cp ${BUILD_ROOT}/kyotocabinet-java-1.24/*.java src/main/java/kyotocabinet/


# to see what dynamic libraries it wants to link to
readelf -d jne/${BUILD_OS}/${BUILD_ARCH}/libjkyotocabinet.so