#!/bin/sh
# Copyright (c) 2021 The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
# BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
# BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
# OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

if (lsmod | grep qca6696);then
	while ! [ "$(ls /sys/class/net/ | grep wifi-aware0)" = "wifi-aware0" ]
	do
		echo  "waiting for wifi-aware0 to be ready"
		sleep 1
	done
fi

if (lsmod | grep qca6390);then
	while ! [ "$(ls /sys/class/net/ | grep wifi-aware1)" = "wifi-aware1" ]
	do
		echo  "waiting for wifi-aware1 to be ready"
		sleep 1
	done
fi

echo "try to delete NAN wifi-aware0 interface..."
if [ -f /sys/class/net/wifi-aware0/operstate ];then
	echo "find wifi-aware0..."
	iw dev wifi-aware0 del
fi

echo "try to delete NAN wifi-aware1 interface..."
if [ -f /sys/class/net/wifi-aware1/operstate ];then
	echo "find wifi-aware1..."
	iw dev wifi-aware1 del
fi
