Java Native Executable - Kyoto Cabinet Library
==============================================

### Contributors

 - [Mfizz, Inc.](http://mfizz.com)
 - Joe Lauer (Twitter: [@jjlauer](http://twitter.com/jjlauer))

### Overview

Drop-in replacement for "kyotocabinet" that includes compiled and embedded
native libraries for Windows (x32/x64; XP and above), Linux (x32/x64; kernel 2.6 and above),
and Mac OSX (x64; 10.5 and above).  The libraries are dynamically extracted and
loaded at runtime via the [mfz-jne](https://github.com/jjlauer/java-jne) support library.
Thus, you can use Kyoto Cabinet in Java without having to install any libraries
on the runtime platform (e.g. jre/ext dir).  The [mfz-jne](https://github.com/jjlauer/java-jne)
has various runtime options for where libraries are extracted, but if you're in
a hurry the default settings will just use a 1-time temporary directory every
time you start your app.

The libraries were compiled on recommended Java MBE (minimum build environment)
operating systems that match what Oracle uses to build the JDK itself. This should
help provide the best possible compatability across OS distros and versions.

The current version of this library uses the following:

 - Kyoto Cabinet v1.2.76 (http://fallabs.com/kyotocabinet)
 - Kyoto Cabinet Java Wrapper v1.24 (http://fallabs.com/kyotocabinet)
 - LZO v2.06 (http://www.oberhumer.com/opensource/lzo)
 - LZMA v5.05 (http://tukaani.org/xz) (* not included on Windows)
 - ZLIB v1.2.8 (http://zlib.net)

### Modifications to Kyoto

The only modification to the underlying kyotocabinet Java library is 1 line in
"kyotocabinet.Loader.java" to use JNE.loadLibrary() rather than System.loadLibrary().
Everything else including the package name "kyotocabinet" remains the same.

### Build Platforms

 - *windows-x64*: Windows XP SP2 x64 edition and Windows 7.1 SDK (MBE - 7.1 SDK has the same
   compilers as VS2010). Tested on XP x32/x64, 7 x64, and 8.1 x64. The Windows
   library does not include support for LZMA compression (but LZO and Z are included).
   See COMPILING_WINDOWS.md for more info.
 - *windows-x32*: Same as windows-x64 but with the /X86 target in Windows 7.1 SDK.
 - *linux-x64*: Ubuntu 8.04 x64 edition (kernel 2.6.24) and GCC 4.2.4.  Tested on
   Ubuntu 12.04 and Ubuntu 13.10 (kernel 3.5.0-37). Library requires shared library
   libz to be on system (seems like its on all base installs of linux) and
   statically included versions of liblzo and liblzma. See COMPILING_LINUX.md for more info.
 - *linux-x32*: Same as linux-x64 but on an x86 host.
 - *osx-x64*: Mac OSX 10.8 x64 targeting 10.5 compatability. See COMPILING_OSX.md for more info.

### Test and Demos

    mvn test

    mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.kyoto.demo.KyotoMain" -Djne.debug="1"

    mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="kyotocabinet.Test" -Dexec.args="wicked target/wicked.kch 2"
