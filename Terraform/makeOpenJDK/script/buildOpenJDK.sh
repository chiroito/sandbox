#!/bin/bash
SRC_BASE_PATH=${1}
echo mount point = ${SRC_BASE_PATH}

cd ${SRC_BASE_PATH}/jtreg
hg pull -u
bash make/build-all.sh /usr/lib/jvm/java-1.8.0

cd ${SRC_BASE_PATH}/jdk
hg pull -u
patch -p1 < /home/opc/myPatch
bash configure --enable-debug --with-native-debug-symbols=internal --with-boot-jdk=/usr/lib/jvm/java-11 --with-jtreg=${SRC_BASE_PATH}/jtreg/build/images/jtreg
time make images
