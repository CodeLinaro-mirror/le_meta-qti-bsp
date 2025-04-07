#!/bin/sh
#=============================================================================
# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#=============================================================================

echo "Thin_driver start"

DIR_THIN=/sys/devices/platform/soc/23049000.qcom,ethernet/net/eth1
exit_status=0

while true
do
	if [ -d "$DIR_THIN" ]
	then
		echo "Thin driver /sys/devices/.../eth1 is created."
		ifconfig eth1 up
		exit_status=$?
		if [ $exit_status -eq 0 ]
		then
			break
		fi
	fi
	sleep 0.05
done
echo "Thin_driver end"