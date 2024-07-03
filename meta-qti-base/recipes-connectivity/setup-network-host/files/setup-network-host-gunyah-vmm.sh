#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

soc_num=`gpioget 0 29`

echo " setup-network-host start "

ip link add name br0 type bridge
ip link set dev br0 up

if [ ${soc_num} -eq 0 ];
then
    ifconfig br0 192.168.1.1 up
else
    ifconfig br0 192.168.1.2 up
fi

echo " setup-network-host bring up br0 successfully on $soc_num"

/lib/systemd/systemd-networkd-wait-online -i eth0:off

echo "eth0 is available now"

ip link set eth0 up
ip link set eth0 master br0

echo " setup-network-host add eth0 into br0 successfully on $soc_num"

echo " setup-network-host end "
