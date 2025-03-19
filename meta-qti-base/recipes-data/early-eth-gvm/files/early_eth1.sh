#!/bin/sh
#=============================================================================
# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#=============================================================================

echo "Thin_driver start"

DIR_THIN=/sys/devices/platform/soc/23049000.qcom,ethernet/net/eth1
exit_status=0
cnt=0

while true
do
	if [ -d "$DIR_THIN" ]
	then
		echo "Thin driver /sys/devices/.../eth1 is created."
		echo 1 > /proc/sys/net/ipv4/conf/all/arp_ignore
		echo 1 > /proc/sys/net/ipv4/conf/default/arp_ignore
		ifconfig eth1 up
		exit_status=$?
		if [ $exit_status -eq 0 ]
		then
			break
		fi
	fi
	cnt=`expr $cnt + 1`
	if [ $cnt -eq 50 ]
	then
		break
	fi
	sleep 0.05
done

echo "Thin_driver end"