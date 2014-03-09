### Overview

Drop-in replacement for "kyotocabinet" that includes compiled and embedded
native libraries for Windows, Linux, and Mac.  The libraries are dynamically
extracted and loaded at runtime via "mfz-jne" support library.  Thus, you can
use Kyoto Cabinet in Java without having to install any native libraries on
the runtime platform.

 - Based on Kyoto Cabinet library 1.2.76
 - Based on Kyoto Cabinet java binding 1.24
 - Includes LZO compression 2.06
 - Includes LZMA compression 5.05

The only modifications this library has made to Kyoto is to replace "Loader.java".
Everything else including the package name "kyotocabinet" remains the same.

### Demos

mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.kyoto.demo.KyotoMain" -Djne.debug="1"

mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="kyotocabinet.Test" -Dexec.args="wicked target/wicked.kch 2"

### Compiling on various platforms

Linux: COMPILING_LINUX.md
