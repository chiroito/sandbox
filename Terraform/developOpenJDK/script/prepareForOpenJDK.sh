#!/bin/bash
SRC_BASE_PATH=${1}
echo mount point = ${SRC_BASE_PATH}

sudo yum groupinstall -y "Development Tools"
sudo yum install -y freetype-devel cups-devel libXtst-devel libXt-devel libXrender-devel libXrandr-devel libXi-devel alsa-lib-devel libffi-devel autoconf java-1.8.0-openjdk-devel fontconfig-devel java-11-openjdk-devel mercurial
echo ${SRC_BASE_PATH}
mkdir -p ${SRC_BASE_PATH}
cd ${SRC_BASE_PATH}
hg clone http://hg.openjdk.java.net/jdk/jdk
hg clone http://hg.openjdk.java.net/code-tools/jtreg
