#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear
set -e

FLAG=/persist/factory_reset.flag
DEV=/dev/disk/by-partlabel/userdata
TAG="factory-reset"
BLOCK_SIZE=4096
FS_SIZE_BYTES="__USERDATA_IMAGE_SIZE__"
FS_SIZE_BLOCKS=$(( FS_SIZE_BYTES / BLOCK_SIZE ))

if [ -f "$FLAG" ]; then
    logger -t "$TAG" "Factory reset triggered, starting..."

    if [ -n "$FS_SIZE_BLOCKS" ] && [ "$FS_SIZE_BLOCKS" -gt 0 ] 2>/dev/null; then
        logger -t "$TAG" "Using explicit size: ${FS_SIZE_BLOCKS} blocks (4KiB each)."
        if ! mkfs.ext4 -E nodiscard -b "$BLOCK_SIZE" -F "$DEV" "$FS_SIZE_BLOCKS"; then
            logger -t "$TAG" "ERROR: Failed to format device. Flag preserved for retry."
            exit 1
        fi
    else
        logger -t "$TAG" "No valid size provided; formatting whole device by default."
        if ! mkfs.ext4 -E nodiscard -b "$BLOCK_SIZE" -F "$DEV"; then
            logger -t "$TAG" "ERROR: Failed to format device. Flag preserved for retry."
            exit 1
        fi
    fi

    rm -f "$FLAG"
    logger -t "$TAG" "Factory reset completed."

fi
exit 0
