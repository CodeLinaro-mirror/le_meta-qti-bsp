#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

LOG_FILE="/var/log/setup_ramjournal.log"
exec > "$LOG_FILE" 2>&1
set -x

# Wait for /dev/ramcarveout, check every 1s, up to 5s
WAIT=0
while [ ! -b /dev/ramcarveout ]; do
    if [ $WAIT -ge 5 ]; then
        echo "ERROR: /dev/ramcarveout not found after 5s, aborting."
        exit 1
    fi
    sleep 1
    WAIT=$((WAIT + 1))
done

mkfs.ext4 /dev/ramcarveout
mkdir -p /data/ramjournal
chown root:systemd-journal /data/ramjournal
chmod 2755 /data/ramjournal
if [ -f /sys/fs/selinux/enforce ]; then
    mount -t ext4 -o sync,context=system_u:object_r:var_log_t:s0 /dev/ramcarveout /data/ramjournal
else
    mount -t ext4 -o sync /dev/ramcarveout /data/ramjournal
fi
mkdir -p /var/volatile/log/journal
mount --bind /data/ramjournal /var/volatile/log/journal
journalctl --flush
