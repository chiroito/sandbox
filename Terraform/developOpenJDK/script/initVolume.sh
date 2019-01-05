#!/bin/bash
iqn=${1}
ipv4=${2}
port=${3}
mp=${4}

echo iqn = ${iqn}
echo ipv4 = ${ipv4}
echo port = ${port}
echo mount point = ${mp}

sudo iscsiadm -m node -o new -T ${iqn} -p ${ipv4}:${port}
sudo iscsiadm -m node -o update -T ${iqn} -n node.startup -v automatic
sudo iscsiadm -m node -T ${iqn} -p ${ipv4}:${port} -l
sudo parted -s /dev/sdb mklabel gpt
sudo parted -s /dev/sdb -- mkpart primary xfs 1 -1
sudo mkfs -t xfs /dev/sdb1
sudo mkdir ${mp}
sudo mount -t xfs /dev/sdb1 ${mp}
sudo chmod 777 ${mp}
