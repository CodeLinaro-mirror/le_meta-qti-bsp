#!/bin/sh

# Copyright (c) 2017, The Linux Foundation. All rights reserved.
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

echo "Checking for firmware corruption"
for dir in `ls /sys/bus/msm_subsys/devices/`;do
        for file in `ls /sys/bus/msm_subsys/devices/$dir`;do
                if [[ $file == "error" ]]; then
                        data=`cat /sys/bus/msm_subsys/devices/$dir/$file`
                        if [[ $data == "firmware_error" ]];then
				mmcblk=`ls -al /dev/disk/by-partlabel | grep "misc"| tr ' ' '\n' | tail -n1`
				if [[ $mmcblk != null ]];then
					cd /dev/disk/by-partlabel
					echo -n "boot-recovery" > $mmcblk
					cookie=`dd if=$mmcblk count=13 bs=1`
					if [[ `echo $cookie` == "boot-recovery" ]];then
						echo "Subsystem $dir firmware is corrupt. Rebooting into recovery kernel"
						reboot
					else
						echo "Error: cookie write failed"
					fi
				else
					echo "Error: misc partition not found"
				fi
			fi
		fi
	done
done
