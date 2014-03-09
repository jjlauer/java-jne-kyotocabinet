
# wget, tar, gzip utils
# http://downloads.sourceforge.net/gnuwin32/wget-1.11.4-1-setup.exe
# http://downloads.sourceforge.net/project/gnuwin32/tar/1.13-1/tar-1.13-1-bin.exe
# http://downloads.sourceforge.net/project/gnuwin32/gzip/1.3.12-1/gzip-1.3.12-1-setup.exe

# dependency walkter is nice to view what libs something uses
# http://www.dependencywalker.com/depends22_x64.zip


# Useful info for wireshark (helps for any command-line builds)
# http://www.wireshark.org/docs/wsdg_html_chunked/ChSetupWin32.html


# On Windows 8:
#  Install Visual Studio C++ Express Edition 2010/2013
#  Install Windows SDK for 8.1

# Then to compile for x64:
#  Run "VS2012 x64 Cross Tools Command Prompt"

# Then to compile for x86:
#  Run "VS2012 x86 Cross Tools Command Prompt"


# On Windows 7 (or XP I believe):
#  Install Microsoft Visual C++ 2010 Express Edition
#  Install Microsoft Windows SDK for Windows 7
#    In case the install of the SDK fails go to software management and remove
#    the VC++ 2010 runtime and redist packages
#  Install Microsoft Visual Studio 2010 Service Pack 1
#  Microsoft Visual C++ 2010 Service Pack 1 Compiler Update for the Windows SDK 7.1

# Then to compile for x64:
#  Run "Windows SDK 7.1 Command Prompt" (under Start Menu -> Microsoft Windows SDK v7.1)
#  SetEnv /Release /x64

# Then to compile for x86:
#  Run "Windows SDK 7.1 Command Prompt" (under Start Menu -> Microsoft Windows SDK v7.1)
#  SetEnv /Release /x86


# include a few utils like wget, tar, gzip
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


# The default VCmakefile unfortunately overrides environment vars
# that Windows SDK by default provides.  Here is how you fix the
# makefile. If you already modified win-x64, just use that one
# xcopy ..\..\win-x64\kyotocabinet-1.2.76\VCmakefile .\

# comment out VCPATH and SDKPATH settings

# change:
# CL = cl
# LIB = lib
# LIN = link

# to:
# CLCMD = cl
# LIBCMD = lib
# LINKCMD = link

# replace $(CL) with $(CLCMD)
# replace $(LIB) with $(LIBCMD)
# replace $(LINK) with $(LINKCMD)


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
# If you already modified win-x64, just use that one
# xcopy ..\..\win-x64\kyotocabinet-java-1.24\VCmakefile .\

# Open file and edit these accordingly
VCPATH = C:\Program Files\Microsoft Visual Studio 10.0\VC
SDKPATH = C:\Program Files\Microsoft SDKs\Windows\v7.0A
JDKPATH = C:\Program Files\Java\jdk1.7.0_51
KCPATH = kcwin32
#
# On my system they were:
#VCPATH = C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC
#SDKPATH = C:\Program Files\Microsoft SDKs\Windows\v7.1
JDKPATH = $(JAVA_HOME)
KCPATH = kcwin32


# then as above...
# change:
# CL = cl
# LIB = lib
# LIN = link

# to:
# CLCMD = cl
# LIBCMD = lib
# LINKCMD = link

# replace $(CL) with $(CLCMD)
# replace $(LIB) with $(LIBCMD)
# replace $(LINK) with $(LINKCMD)



nmake -f VCmakefile


# jkyotocabinet.dll will not be in the directory

# copy x64
xcopy jkyotocabinet.dll ..\..\..\jne\windows\x64\

# copy x32
xcopy jkyotocabinet.dll ..\..\..\jne\windows\x32\

