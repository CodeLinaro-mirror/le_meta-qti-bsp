#!/bin/sh

# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

devices="/dev/kgsl-3d0 /dev/video32 /dev/video33 \
         /dev/dri/renderD128 /dev/hwrng /dev/qseecom "

for var in $devices; do
    if [ -c $var ] || [ -b $var ]; then
        cp -dpR $var ${LXC_ROOTFS_MOUNT}/dev
    fi
done
