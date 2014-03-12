
## Compiling on Mac OSX

### Prerequisites

#### Build tools

Install Oracle JDK 7 (build 51 as of this writing)

    http://java.oracle.com/

JAVA_HOME is set to your JDK

    export JAVA_HOME=$(/usr/libexec/java_home)

#### Set environment vars for build:

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

#### Create directories for library sources build

Navigate to your project directory. For example, "/home/joelauer/workspace/java-jne-kyotocabinet".

    export BUILD_ROOT=native/${BUILD_OS}-${BUILD_ARCH}
    mkdir -p ${BUILD_ROOT}/sysroot
    cd ${BUILD_ROOT}

#### Realpath

Create "realpath" script to mimic command from linux

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

Set it to executable

    chmod +x ./realpath

Include on your path

    export PATH=$PATH:$PWD

### Build

#### Build LZO static library

Download and extract sources

    wget http://www.oberhumer.com/opensource/lzo/download/lzo-${VER_LZO}.tar.gz
    tar zxvf lzo-${VER_LZO}.tar.gz
    cd lzo-${VER_LZO}

To configure and build library

    CFLAGS="-mmacosx-version-min=10.5" \
    LDFLAGS="-mmacosx-version-min=10.5" ABI=32 \
    ./configure --prefix=$(realpath ../sysroot) --enable-static --disable-shared --with-pic 
    make
    make install

Return to root build

    cd ..

#### Build LZMA static library

Download and extract sources

    wget http://tukaani.org/xz/xz-${VER_LZMA}.tar.gz
    tar zxvf xz-${VER_LZMA}.tar.gz
    cd xz-${VER_LZMA}

To configure and build library

    CFLAGS="-mmacosx-version-min=10.5" \
    LDFLAGS="-mmacosx-version-min=10.5" ABI=32 \
    ./configure --prefix=$(realpath ../sysroot) --disable-xz --disable-xzdec --disable-lzmadec --disable-lzmainfo --disable-shared --with-pic
    make
    make install

Return to root build

    cd ..

#### Build KYOTOCABINET static library

Download and extract sources

    wget http://fallabs.com/kyotocabinet/pkg/kyotocabinet-${VER_KC}.tar.gz
    tar zxvf kyotocabinet-${VER_KC}.tar.gz
    cd kyotocabinet-${VER_KC}

To configure and build library

    CPPFLAGS="-mmacosx-version-min=10.5 -I$(realpath ../sysroot/include)" \
    LDFLAGS="-mmacosx-version-min=10.5 -L$(realpath ../sysroot/lib)" ABI=32 \
    ./configure --prefix=$(realpath ../sysroot) --enable-lzo --enable-lzma
    make
    make install

Return to root build

    cd ..

#### Prep for Java native library build

Delete all dylibs to force linking to static libs only

    rm -Rf sysroot/lib/*.dylib

#### Build KYOTOCABINET Java native dynamic library

Download and extract sources

    wget http://fallabs.com/kyotocabinet/javapkg/kyotocabinet-java-${VER_KC_JAVA}.tar.gz
    tar zxvf kyotocabinet-java-${VER_KC_JAVA}.tar.gz
    cd kyotocabinet-java-${VER_KC_JAVA}

To configure build

    LIBS="-llzo2 -llzma" \
    CXXFLAGS="-mmacosx-version-min=10.5 -I$JAVA_HOME/include -I$JAVA_HOME/include/darwin" \
    LDFLAGS="-mmacosx-version-min=10.5" ABI=32 \
    ./configure --with-kc=$(realpath ../sysroot) --prefix=$(realpath ../sysroot)

Kyoto's configure script is broken slightly on OSX, tweak it

    sed -i -e "s/Contents\/Home\/include\/mac/Contents\/Home\/include\/darwin/" Makefile
    sed -i -e "s/CXXFLAGS = /CXXFLAGS = -mmacosx-version-min=10.5 /" Makefile

To build library

    make
    make install

#### Copy Java native dynamic library

    mkdir -p ../../jne/${BUILD_OS}/${BUILD_ARCH}
    cp ${BUILD_ROOT}/sysroot/lib/libjkyotocabinet.dylib ../../jne/${BUILD_OS}/${BUILD_ARCH}/

Return to project

    cd ../..

#### See what final library links to?

    otool -L jne/${BUILD_OS}/${BUILD_ARCH}/libjkyotocabinet.dylib