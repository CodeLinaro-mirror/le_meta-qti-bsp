#!/bin/sh
# Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

RUST_BACKTRACE=full qcrosvm \
--vm=autoghgvm \
--disk=/dev/disk/by-partlabel/la_super,label=17,rw=true \
--disk=/dev/disk/by-partlabel/la_userdata,label=16,rw=true \
--disk=/dev/disk/by-partlabel/la_metadata,label=15,rw=true \
--disk=/dev/disk/by-partlabel/la_persist,label=11,rw=true \
--disk=/dev/disk/by-partlabel/la_misc,label=10,rw=true \
--net=true,label=13,ip_addr=192.168.1.13,netmask=255.255.255.0,mac=5A:6F:F0:05:7C:24 \
--vhost-user-hab "/tmp/linux-vm2-disp-skt",label=1A,device-id=93,queue-num=10 \
--vhost-user-hab "/tmp/linux-vm2-ogles-skt",label=14,device-id=94,queue-num=2 \
--vhost-user-hab "/tmp/linux-vm2-misc-skt",label=18,device-id=90,queue-num=2 \
--vhost-user-hab "/tmp/linux-vm2-aud-skt",label=19,device-id=91,queue-num=8 \
--vhost-user-hab "/tmp/linux-vm2-vid-skt",label=1B,device-id=95,queue-num=6 \
--vhost-user-hab "/tmp/linux-vm2-cam-skt",label=1C,device-id=92,queue-num=4 \
--input=/dev/input/event2,label=1D &

sleep 2
ip link add name br0 type bridge
ip link set br0 up
ip link set dev eth0 master br0
ip link set dev vmtap0 master br0
ip addr add 192.168.1.1/24 dev br0
ifconfig vmtap0 0.0.0.0
ifconfig eth0 0.0.0.0
