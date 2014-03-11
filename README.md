### Overview

Drop-in replacement for "kyotocabinet" that includes compiled and embedded
native libraries for Windows (x32/x64), Linux (x32/x64), and Mac (x64).  The
libraries are dynamically extracted and loaded at runtime via the "mfz-jne"
support library.  Thus, you can use Kyoto Cabinet in Java without having to
install any libraries on the runtime platform (e.g. jre/ext dir).

The libraries were compiled on recommended Java MBE (minimum build environment)
operating systems that match what Oracle uses to build the JDK itself. This should
help provide the best possible compatability across OS versions.

The current version of this library uses the following:

 - Kyoto Cabinet v1.2.76 (http://fallabs.com/kyotocabinet)
 - Kyoto Cabinet Java Wrapper v1.24 (http://fallabs.com/kyotocabinet)
 - LZO v2.06 (http://www.oberhumer.com/opensource/lzo)
 - LZMA v5.05 (http://tukaani.org/xz)
 - ZLIB v1.2.8 (http://zlib.net)

The only modifications this library has made to Kyoto is to replace "Loader.java".
Everything else including the package name "kyotocabinet" remains the same.

#### Windows

Compiled on Windows XP SP2 x64 host for both x32 and x64 targets. Tested on XP,
Windows 7, and Windows 8.1. The Windows library does not include LZMA due to
issues with Microsoft Visual C++ compiler and the LZMA-provided library compiled
with GCC.

See COMPILING_WINDOWS.md for more info.

#### Linux

Compiled on Ubuntu 8.04 x32 and x64 hosts (kernel 2.6.24). Also, tested on Ubuntu 12.04
and Ubuntu 13.10 (kernel 3.5.0-37). Library includes shared link to libz (seems
like its by default on every linux distro) and statically compiled versions of
liblzo and liblzma. If you happen to try and use another Java native library
that also uses these libraries, you'll end up with a runtime error.  Since I 
think the likelihood of most users hitting this case is remote, I took the ease
of use of statically compiling it in vs. forcing you to install the libraries
on the target machine or requiring you to set LD_LIBRARY_PATH for Java.

See COMPILING_LINUX.md for more info.

#### Mac OSX



### Demos

mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.kyoto.demo.KyotoMain" -Djne.debug="1"

mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="kyotocabinet.Test" -Dexec.args="wicked target/wicked.kch 2"

### Compiling on various platforms


