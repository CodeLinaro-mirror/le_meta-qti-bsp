#!/bin/sh
# Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

DEVS="17c23000.vfio_timer ae00000.vfio_dpu_00 aa00000.vfio_vidc 408000.umd_pil soc:vfio_vidc_non_secure_pixel_cb 90d80000.sail-mailbox 90e00000.sail-mailbox-ota ac00000.vfio_titan_base"

for DEV in $DEVS; do
    echo "vfio-platform" > /sys/bus/platform/devices/$DEV/driver_override
    echo $DEV > /sys/bus/platform/drivers/vfio-platform/bind
done
