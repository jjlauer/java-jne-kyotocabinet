
## Compiling on Windows

### Prerequisites

#### Command-line tools for build (using windows command prompt)

    http://downloads.sourceforge.net/gnuwin32/wget-1.11.4-1-setup.exe
    http://downloads.sourceforge.net/project/gnuwin32/tar/1.13-1/tar-1.13-1-bin.exe
    http://downloads.sourceforge.net/project/gnuwin32/gzip/1.3.12-1/gzip-1.3.12-1-setup.exe
    http://downloads.sourceforge.net/project/gnuwin32/unzip/5.51-1/unzip-5.51-1.exe
    http://downloads.sourceforge.net/project/gnuwin32/sed/4.2.1/sed-4.2.1-setup.exe

#### Dependency walker (like linux ldd / mac otool)

    http://www.dependencywalker.com/depends22_x64.zip

#### Wireshark project has great doc on setup of MS tools

    http://www.wireshark.org/docs/wsdg_html_chunked/ChSetupWin32.html

#### Windows 8

1. Install Visual Studio C++ Express Edition 2010/2013
2. Install Windows SDK for 8.1
3. Open up correct command prompt
    - To compile for x64:
        Run "VS2012 x64 Cross Tools Command Prompt"
    - To compile for x86:
        Run "VS2012 x86 Cross Tools Command Prompt"

#### Windows 7 (or XP):

1. Install Microsoft Visual C++ 2010 Express Edition
2. Install Microsoft Windows SDK for Windows 7
    - In case the install of the SDK fails go to software management and remove
    the VC++ 2010 runtime and redist packages.
3. Install Microsoft Visual Studio 2010 Service Pack 1
4. Microsoft Visual C++ 2010 Service Pack 1 Compiler Update for the Windows SDK 7.1
5. Open up correct command prompt:
    - To compile for x64:
        - Run "Windows SDK 7.1 Command Prompt" (under Start Menu -> Microsoft Windows SDK v7.1)
        - SetEnv /Release /x64 /XP
    - To compile for x86:
        - Run "Windows SDK 7.1 Command Prompt" (under Start Menu -> Microsoft Windows SDK v7.1)
        - SetEnv /Release /x86 /XP

#### Command-line tools to PATH environment var

Add using "Computer -> Properties -> Environment Vars" or add to path in your
command prompt:

    set PATH=%PATH%;"C:\Program Files (x86)\GnuWin32\bin"

#### Set build target

For x64:

    set BUILD_ARCH=x64

For x32:

    set BUILD_ARCH=x32

#### Create directories for library sources build

Navigate to your project directory. For example, "C:\Users\Joe Lauer\workspace\java-jne-kyotocabinet".

    mkdir native
    mkdir native\win-%BUILD_ARCH%
    cd native\win-%BUILD_ARCH%


#### Build LZO static library

Download and extract sources

    wget http://www.oberhumer.com/opensource/lzo/download/lzo-2.06.tar.gz
    gzip -d lzo-2.06.tar.gz
    tar xvf lzo-2.06.tar
    cd lzo-2.06

Configure and make x64 library
    
    B\win64\vc.bat

Configure and make x32 library

    B\win32\vc.bat

Return to root build

    cd ..


# Z lib
wget http://zlib.net/zlib-1.2.8.tar.gz
gzip -d zlib-1.2.8.tar.gz
tar xvf zlib-1.2.8.tar
cd zlib-1.2.8

# to build for x64 (with assembly code)
nmake -f win32/Makefile.msc AS=ml64 LOC="-DASMV -DASMINF -I." OBJA="inffasx64.obj gvmat64.obj inffas8664.obj"

# to build for x32 (with assembly code)
nmake -f win32/Makefile.msc LOC="-DASMV -DASMINF" OBJA="inffas32.obj match686.obj"



# SKIP THIS ON WINDOWS!
# LZMA lib (already pre-built libraries for windows!!!)
md xz-5.0.5-windows
cd xz-5.0.5-windows
wget http://tukaani.org/xz/xz-5.0.5-windows.zip
unzip xz-5.0.5-windows.zip



# Build kyotocabinet.lib
wget http://fallabs.com/kyotocabinet/pkg/kyotocabinet-1.2.76.tar.gz
gzip -d kyotocabinet-1.2.76.tar.gz
tar xvf kyotocabinet-1.2.76.tar
cd kyotocabinet-1.2.76


# The default VCmakefile unfortunately overrides environment vars
# that Windows SDK by default provides.  Here is how you fix the
# makefile. If you already modified win-x64, just use that one
# xcopy ..\..\win-x64\kyotocabinet-1.2.76\VCmakefile .\

# use sed to correct the VCmakefile
sed -i -e "s/VCPATH \= /#VCPATH \= /" VCmakefile
sed -i -e "s/SDKPATH \= /#SDKPATH \= /" VCmakefile
sed -i -e "s/CL = cl/CLCMD = cl/" VCmakefile
sed -i -e "s/LIB = lib/LIBCMD = lib/" VCmakefile
sed -i -e "s/LINK = link/LINKCMD = link/" VCmakefile
sed -i -e "s/\$(CL)/\$(CLCMD)/" VCmakefile
sed -i -e "s/\$(LIB)/\$(LIBCMD)/" VCmakefile
sed -i -e "s/\$(LINK)/\$(LINKCMD)/" VCmakefile
sed -i -e "s/\tcopy \*.exe/#\tcopy *.exe/" VCmakefile

# to enable LZO and ZLIB
sed -i -e "s/\/D_CRT_SECURE_NO_WARNINGS \\/\/D_CRT_SECURE_NO_WARNINGS \/D_MYLZO \/I \"..\\lzo-2.06\\include\" \/D_MYZLIB \/I \"..\\zlib-1.2.8\" \\/" VCmakefile

nmake -f VCmakefile kyotocabinet.lib
nmake -f VCmakefile binpkg


cd ..


# java wrapper to lib
wget http://fallabs.com/kyotocabinet/javapkg/kyotocabinet-java-1.24.tar.gz
gzip -d kyotocabinet-java-1.24.tar.gz
tar xvf kyotocabinet-java-1.24.tar
cd kyotocabinet-java-1.24


# copy libs from kyotolib
xcopy /s ..\kyotocabinet-1.2.76\kcwin32 .\kcwin32

# copy libs from lzo
xcopy /s ..\lzo-2.06\*.lib .\kcwin32\lib
# copy libs from zlib
xcopy /s ..\zlib-1.2.8\*.lib .\kcwin32\lib



# The default VCmakefile has the wrong paths to the sdks, java, etc...
sed -i -e "s/VCPATH \= /#VCPATH \= /" VCmakefile
sed -i -e "s/SDKPATH \= /#SDKPATH \= /" VCmakefile
sed -i -e "s/JDKPATH \= .*/JDKPATH \= \$(JAVA_HOME)/" VCmakefile
sed -i -e "s/CL = cl/CLCMD = cl/" VCmakefile
sed -i -e "s/LIB = lib/LIBCMD = lib/" VCmakefile
sed -i -e "s/LINK = link/LINKCMD = link/" VCmakefile
sed -i -e "s/\$(CL)/\$(CLCMD)/" VCmakefile
sed -i -e "s/\$(LIB)/\$(LIBCMD)/" VCmakefile
sed -i -e "s/\$(LINK)/\$(LINKCMD)/" VCmakefile

# Add linkage to LZO and ZLIB
sed -i -e "s/kcwin32\\lib\\kyotocabinet.lib/\/NODEFAULTLIB:MSVCRT \$(KCPATH)\\lib\\kyotocabinet.lib \$(KCPATH)\\lib\\lzo2.lib \$(KCPATH)\\lib\\zlib.lib/" VCmakefile

nmake -f VCmakefile


# jkyotocabinet.dll will not be in the directory
xcopy jkyotocabinet.dll ..\..\..\jne\windows\%BUILD_ARCH%\


## Testing x64 and x32 on same system
# Assuming you are on x64 by default with an x64 of Java - do your normal testing
# To override to x32 Java, just do the following:
set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.7.0_51
set PATH=%JAVA_HOME%\bin;%PATH%

