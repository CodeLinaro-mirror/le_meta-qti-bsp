#!/bin/sh
#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear

SLOT_SUFFIX=$(/bin/echo $SLOT_SUFFIX)

MAPDEV=""
DEVICE=""
if [[ "$1" == "-p" ]]; then
    MAPDEV=$2
    shift; shift
fi

if [[ "$1" == "-d" ]]; then
    DEVICE=$2
    DEVICE="/dev/disk/by-partlabel/$2"
    shift; shift
fi
if [ -f /verity/$MAPDEV.env ]; then
   source /verity/$MAPDEV.env
   /usr/sbin/veritysetup open $DEVICE$SLOT_SUFFIX $MAPDEV \
       $DEVICE$SLOT_SUFFIX $VERITY_ROOT_HASH --salt=$VERITY_SALT --hash=$VERITY_HASH_ALGORITHAM\
       --hash-offset=$VERITY_HASH_OFFSET --data-block-size=$VERITY_DATA_BLOCK_SIZE --data-blocks=$VERITY_DATA_BLOCKS --hash-block-size=$VERITY_HASH_BLOCK_SIZE\
       --fec-device=$DEVICE$SLOT_SUFFIX --fec-offset=$VERITY_FEC_OFFSET \
       --fec-roots=$VERITY_FEC_ROOTS --no-superblock --restart-on-corruption

   if [ $? -ne 0 ]; then
      echo "Veritysetup failed."
      echo "Rebooting the device."
      systemctl reboot "dm-verity device corrupted" -f
   else
      echo "verity setup is sucessful"
   fi

   # veritysetup doesn't create symlink to /dev/dm-X as expected by udev, do it explicitly
   if [ -f /dev/dm-0 ] ; then
      /bin/ln -sf ../dm-1 /dev/mapper/$MAPDEV
   else
      /bin/ln -sf ../dm-0 /dev/mapper/$MAPDEV
   fi

   echo "/dev/mapper/$MAPDEV ready"
   if [ $? -ne 0 ]; then
      echo "dm-verity error: symlink to /dev/mapper/$MAPDEV has failed. Rebooting the device."
      systemctl reboot "dm-verity device corrupted" -f
   fi
else
   echo "/verity/$MAPDEV.env not found. Rebooting the device."
   systemctl reboot "dm-verity device corrupted" -f
fi

