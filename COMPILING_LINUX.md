
## Compiling on Linux

### Prerequisites

#### Build tools

On Ubuntu:

    sudo apt-get install build-essential checkinstall realpath libz-dev

JAVA_HOME environment var is set to your JDK:

    export JAVA_HOME=path_to_java_home

#### Set environment vars for build:

    export BUILD_OS=linux
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

### Build

#### Build LZO static library

Download and extract sources

    wget http://www.oberhumer.com/opensource/lzo/download/lzo-${VER_LZO}.tar.gz
    tar zxvf lzo-${VER_LZO}.tar.gz
    cd lzo-${VER_LZO}

To configure and build library

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

    CPPFLAGS="-I$(realpath ../sysroot/include) -static" \
    LDFLAGS="-L$(realpath ../sysroot/lib) -static" \
    ./configure --prefix=$(realpath ../sysroot) --enable-static --disable-shared --enable-lzo --enable-lzma
    make
    make install

Return to root build

    cd ..

#### Build KYOTOCABINET Java native dynamic library

Download and extract sources

    wget http://fallabs.com/kyotocabinet/javapkg/kyotocabinet-java-${VER_KC_JAVA}.tar.gz
    tar zxvf kyotocabinet-java-${VER_KC_JAVA}.tar.gz
    cd kyotocabinet-java-${VER_KC_JAVA}

To configure and build library

    LIBS="-llzo2 -llzma" \
    CPPFLAGS="-I$JAVA_HOME/include" \
    ./configure --with-kc=$(realpath ../sysroot) --prefix=$(realpath ../sysroot)
    make
    make install

Return to root build

    cd ..

#### Copy Java native dynamic library

    mkdir -p ../../jne/${BUILD_OS}/${BUILD_ARCH}
    cp ${BUILD_ROOT}/sysroot/lib/libjkyotocabinet.so ../../jne/${BUILD_OS}/${BUILD_ARCH}/

Return to project

    cd ../..

#### Update Java sources (not required most of the time)

    rm ${BUILD_ROOT}/kyotocabinet-java-1.24/Loader.java
    mv ${BUILD_ROOT}/kyotocabinet-java-1.24/Test.java src/test/java/kyotocabinet/
    cp ${BUILD_ROOT}/kyotocabinet-java-1.24/*.java src/main/java/kyotocabinet/

#### See what final library links to?

    readelf -d jne/${BUILD_OS}/${BUILD_ARCH}/libjkyotocabinet.so