#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

soc_num=`gpioget 0 29`

echo " setup-network-host start "

/lib/systemd/systemd-networkd-wait-online -i eth0:off

echo "eth0 is available now"

if [ ${soc_num} -eq 0 ];
then
    ifconfig eth0 192.168.1.1 up
else
    ifconfig eth0 192.168.1.2 up
fi

echo " setup-network-host bring up eth0 successfully on $soc_num"

echo " setup-network-host end "
