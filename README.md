### Overview

Native executable "ffmpeg" to be easily embedded and used from Java as a dependency.
The upstream ffmpeg version that is included in this jar will match the version
of this jar. So "mfz-jne-ffmpeg-2.1.4.jar" will be based on ffmpeg v2.1.4.


### Static builds of ffmpeg

#### Windows

Supports x86/x64 version >= XP
http://ffmpeg.zeranoe.com/builds/

#### Mac OSX

Supports x64 version >= 10.6., 10.7., 10.8. or 10.9 (Snow Leopard, Lion, Mountain Lion and Mavericks)
http://ffmpegmac.net/

#### Linux

Supports x86/x64 version >= 3.2.x
http://ffmpeg.gusari.org/static/

### Demos

mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.mfizz.kyoto.demo.KyotoMain" -Djne.debug=true

mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="kyotocabinet.Test"mvn -e test-compile exec:java -Dexec.classpathScope="test" -Dexec.mainClass="kyotocabinet.Test" -Dexec.args="wicked target/wicked.kch 2"

### Compiling on various platforms

Linux: FFMPEG_LINUX.md