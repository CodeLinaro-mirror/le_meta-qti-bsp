#!/bin/sh
# Copyright (c) 2014-2017, The Linux Foundation. All rights reserved.
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
#
# find_recovery_partitions        init.d script to dynamically find partitions used in recovery
#

fstab_file=/res/recovery_volume_detected

UpdateRecoveryVolume () {
   partition=$1
   dir=$2
   fs_type=$3
   device=$4
   echo "$device       $dir     $fs_type     defaults    0   0" >> $fstab_file
}

CreateSymLink () {
   partition=$1
   dir=$2
   fstab_only="$3"

   mmc_block_device=/dev/disk/by-partlabel/$partition

   echo "EMMC : Looking for raw : $dir for $partition"
   mkdir -p $dir
   ln -s $mmc_block_device $dir
   UpdateRecoveryVolume $1 $2 "emmc" $mmc_block_device
}

#wait til /dev/disk/by-partlebel ready
/bin/udevadm settle

mkdir -p /data
mount /dev/disk/by-partlabel/euserdata /data

mkdir -p /cache
mount /dev/disk/by-partlabel/ecache /cache

eval CreateSymLink misc /misc

mkdir -p /firmware
mount /dev/disk/by-partlabel/modem /firmware

# wait mount action ready
sleep 1

ls /dev/disk/by-partlabel/*bak
echo Copy all A partitions to B partitions ...
for bak in /dev/disk/by-partlabel/*bak
do
  if [ -e ${bak} ] ; then
	org=`echo -n "${bak}" | awk '{print substr($0,1,length()-3)}'`
	echo Copy ${org} to ${bak} ...
	dd of=${bak} if=${org}
  fi
done
echo Copy A to B done!

exit
