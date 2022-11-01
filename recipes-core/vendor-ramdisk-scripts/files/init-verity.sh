#!/bin/sh
#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
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
       $DEVICE$SLOT_SUFFIX $VERITY_ROOT_HASH --salt $VERITY_SALT \
       --hash-offset $VERITY_HASH_OFFSET --data-blocks $VERITY_DATA_BLOCKS \
       --fec-device $DEVICE$SLOT_SUFFIX --fec-offset $VERITY_FEC_OFFSET \
       --fec-roots $VERITY_FEC_ROOTS --root-hash-signature=/verity/"$MAPDEV".sig

   if [$? -ne 0 ]; then
	   echo "verity setup was sucess"
   fi
   # veritysetup doesn't create symlink to /dev/dm-X as expected by udev, do it explicitly
   if [ -f /dev/dm-0 ] ; then
      /bin/ln -sf ../dm-1 /dev/mapper/$MAPDEV
   else
      /bin/ln -sf ../dm-0 /dev/mapper/$MAPDEV
   fi
   echo "/dev/mapper/$MAPDEV ready"
else
   echo "/verity/$MAPDEV.env not found. Exiting..."
fi

if [ $? -ne 0 ]; then
    echo "mounting /dev/mapper/$MAPDEV failed"
fi
