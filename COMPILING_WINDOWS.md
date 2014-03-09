
# wget, tar, gzip utils
# http://downloads.sourceforge.net/gnuwin32/wget-1.11.4-1-setup.exe
# http://downloads.sourceforge.net/project/gnuwin32/tar/1.13-1/tar-1.13-1-bin.exe
# http://downloads.sourceforge.net/project/gnuwin32/gzip/1.3.12-1/gzip-1.3.12-1-setup.exe

# dependency walkter is nice to view what libs something uses
# http://www.dependencywalker.com/depends22_x64.zip


# Useful info for wireshark (helps for any command-line builds)
# http://www.wireshark.org/docs/wsdg_html_chunked/ChSetupWin32.html

######### Using Visual Studio C++ 2010 Express Edition
Start Menu -> Microsoft Visual Studio 2010 Express -> Visual Studio Command Prompt (2010)

# To build 32-bit binaries call:
C:\Program Files\Microsoft SDKs\Windows\v7.1\Bin\SetEnv.Cmd" /Release /x86

# To build 64-bit binaries call:
"C:\Program Files\Microsoft SDKs\Windows\v7.1\Bin\SetEnv.Cmd" /Release /x64

# add gnu utils to path
set PATH=%PATH%;"C:\Program Files (x86)\GnuWin32\bin"


# LZO lib
wget http://www.oberhumer.com/opensource/lzo/download/lzo-2.06.tar.gz


# LZMA lib
wget http://tukaani.org/xz/xz-5.0.5.tar.gz


# 
wget http://fallabs.com/kyotocabinet/pkg/kyotocabinet-1.2.76.tar.gz
gzip -d kyotocabinet-1.2.76.tar.gz
tar xvf kyotocabinet-1.2.76.tar
cd kyotocabinet-1.2.76


# The default VCmakefile has the wrong paths to the microsoft sdks...
#
# Open VCmakefile and set these accordingly
# VCPATH = C:\Program Files\Microsoft Visual Studio 10.0\VC
# SDKPATH = C:\Program Files\Microsoft SDKs\Windows\v7.0A
#
# On my system they were:
# VCPATH = C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC
# SDKPATH = C:\Program Files\Microsoft SDKs\Windows\v7.1


nmake -f VCmakefile
nmake -f VCmakefile binpkg


cd ..


# java wrapper to lib
wget http://fallabs.com/kyotocabinet/javapkg/kyotocabinet-java-1.24.tar.gz
gzip -d kyotocabinet-java-1.24.tar.gz
tar xvf kyotocabinet-java-1.24.tar
cd kyotocabinet-java-1.24


# copy libs from kyotolib
xcopy /s ..\kyotocabinet-1.2.76\kcwin32 .\kcwin32


# The default VCmakefile has the wrong paths to the sdks, java, etc...
#
# Open file and edit these accordingly
# VCPATH = C:\Program Files\Microsoft Visual Studio 10.0\VC
# SDKPATH = C:\Program Files\Microsoft SDKs\Windows\v7.0A
# JDKPATH = C:\Program Files\Java\jdk1.7.0_51
# KCPATH = kcwin32
#
# On my system they were:
# VCPATH = C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC
# SDKPATH = C:\Program Files\Microsoft SDKs\Windows\v7.1
# JDKPATH = C:\Program Files\Java\jdk1.7.0_51
# KCPATH = kcwin32


nmake -f VCmakefile


# jkyotocabinet.dll will not be in the directory
