#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

echo " setup-network-host start "

/lib/systemd/systemd-networkd-wait-online -i eth0:off

echo "eth0 is available now"

ip link set eth0 up
ifconfig eth0 192.168.1.1 up

echo " setup-network-host end "
