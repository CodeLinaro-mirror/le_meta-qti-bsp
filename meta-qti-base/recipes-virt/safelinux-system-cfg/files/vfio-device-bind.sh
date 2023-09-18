#!/bin/sh
# Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

DEVS="17c23000.vfio_timer"

for DEV in $DEVS; do
    echo "vfio-platform" > /sys/bus/platform/devices/$DEV/driver_override
    echo $DEV > /sys/bus/platform/drivers/vfio-platform/bind
done
