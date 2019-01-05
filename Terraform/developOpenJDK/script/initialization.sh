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
echo sudo iscsiadm -m node -T ${iqn} -p ${ipv4}:${port} -l >> ~/.bashrc
sudo mount -t xfs /dev/sdb1 ${mp}
sudo chmod 777 ${mp}
sudo sed -i s/4096/unlimited/ /etc/security/limits.d/20-nproc.conf
sudo sed -i '1s/^/*    soft nofile 65535\n/' /etc/security/limits.conf
sudo sed -i '1s/^/*    hard nofile 65535\n/' /etc/security/limits.conf
