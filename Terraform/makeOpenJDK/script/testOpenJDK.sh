#!/bin/bash
SRC_BASE_PATH=${1}
echo mount point = ${SRC_BASE_PATH}

cd ${SRC_BASE_PATH}/jdk
time make test-tier1
time make test TEST="jdk/jfr"
