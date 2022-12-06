#!/bin/sh
# Copyright (c) 2019, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#   * Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#   * Redistributions in binary form must reproduce the above
#     copyright notice, this list of conditions and the following
#     disclaimer in the documentation and/or other materials provided
#     with the distribution.
#   * Neither the name of The Linux Foundation nor the names of its
#     contributors may be used to endorse or promote products derived
#     from this software without specific prior written permission.
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
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

GetFirmwareVolumeID () {
    firmware=$1
    vid=0
    act_slot=`cat /proc/cmdline | sed 's/.*SLOT_SUFFIX=//' | awk '{print $1}'`
    firmware_ab_name=${firmware}${act_slot}
    vid=`ubinfo -d 0 -N ${firmware} | grep -iw "volume ID" | awk -F ':' '{print $2}' | awk -F ' ' '{print $1}'`
    if [ "$vid" == "" ]; then
      vid=`ubinfo -d 0 -N ${firmware_ab_name} | grep -iw "volume ID" | awk -F ':' '{print $2}' | awk -F ' ' '{print $1}'`
    fi
    echo $vid
}

FindAndMountUBIVol () {
   partition=$1
   dir=$2

   volid=$(GetFirmwareVolumeID $partition)
   if [ "$volid" == "" ]; then
       return
   fi

   device=/dev/ubi0_$volid
   block_device=/dev/ubiblock0_$volid
    mkdir -p $dir

   ubiblock --create $device
   mount -t squashfs $block_device $dir -o ro
   if [ $? -ne 0 ] ; then
      echo "Unable to mount squashfs onto ubiblock0_$volumeindex."
      exit 1
   fi
}

FindAndMountUBI () {
   partition=$1
   dir=$2

   mtd_block_number=`cat $mtd_file | grep -i $partition | sed 's/^mtd//' | awk -F ':' '{print $1}'`
   echo "MTD : Detected block device : $dir for $partition"
   mkdir -p $dir

   ubiattach -m $mtd_block_number
   non_hlos_block=`cat $mtd_file | grep -i nonhlos-fs | sed 's/^mtd//' | awk -F ':' '{print $1}'`
   device=/dev/mtdblock$non_hlos_block
   while [ 1 ]
    do
        if [ -b $device ]
        then
            mount $device /firmware
            break
        else
            sleep 0.010
        fi
    done
}

mtd_file=/proc/mtd
nad_ubi_present=`cat $mtd_file | grep nad_ubi | wc -l`

if [ $nad_ubi_present -eq 0 ]; then
   eval FindAndMountUBI modem /firmware
else
   eval FindAndMountUBIVol firmware /firmware
fi
exit 0
