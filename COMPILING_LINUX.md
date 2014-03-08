mkdir -p build/linux-x64/sysroot
cd build/linux-x64


# LZO lib
wget http://www.oberhumer.com/opensource/lzo/download/lzo-2.06.tar.gz
tar zxvf lzo-2.06.tar.gz
cd lzo-2.06
./configure --prefix=$(realpath ../sysroot) --enable-static --disable-shared --with-pic
make
make install


cd ..


# LZMA lib
wget http://tukaani.org/xz/xz-5.0.5.tar.gz
tar zxvf xz-5.0.5.tar.gz
cd xz-5.0.5
./configure --prefix=$(realpath ../sysroot) --disable-xz --disable-xzdec --disable-lzmadec --disable-lzmainfo --disable-shared --with-pic
make
make install


cd ..


wget http://fallabs.com/kyotocabinet/pkg/kyotocabinet-1.2.76.tar.gz
tar zxvf kyotocabinet-1.2.76.tar.gz
cd kyotocabinet-1.2.76

CPPFLAGS="-I$(realpath ../sysroot/include) -static" \
LDFLAGS="-L$(realpath ../sysroot/lib) -static" \
./configure --prefix=$(realpath ../sysroot) --enable-static --disable-shared --enable-lzo --enable-lzma

make
make install


cd ..


# java wrapper to lib
wget http://fallabs.com/kyotocabinet/javapkg/kyotocabinet-java-1.24.tar.gz
tar zxvf kyotocabinet-java-1.24.tar.gz
cd kyotocabinet-java-1.24

LIBS="-llzo2 -llzma" \
./configure --with-kc=$(realpath ../sysroot) --prefix=$(realpath ../sysroot)

make
make install


# correct libjkyotocabinet.so now installed in ../sysroot/lib

cd ../../..
mkdir -p jne/linux/x64
cp build/linux-x64/sysroot/lib/libjkyotocabinet.so jne/linux/x64/


# copy everything but Loader.java and Test.java to main kyotocabinet...
rm build/linux-x64/kyotocabinet-java-1.24/Loader.java
mv build/linux-x64/kyotocabinet-java-1.24/Test.java src/test/java/kyotocabinet/
cp build/linux-x64/kyotocabinet-java-1.24/*.java src/main/java/kyotocabinet/
