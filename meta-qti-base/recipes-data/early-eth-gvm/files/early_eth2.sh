#!/bin/sh
#=============================================================================
# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#=============================================================================

echo "Passthru driver start"

DIR=/sys/devices/platform/soc/23000000.qcom,ethernet/net/eth2
exit_status=0
cnt=0

while true
do
	if [ -d "$DIR" ]
	then
		echo "Passthru driver /sys/devices/.../eth2 is created."
		ifconfig eth2 up
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

echo "Passthru driver end"